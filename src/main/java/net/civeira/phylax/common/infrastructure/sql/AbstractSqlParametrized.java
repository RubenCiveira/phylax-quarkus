/* @autogenerated */
package net.civeira.phylax.common.infrastructure.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import io.opentelemetry.api.trace.Span;

public abstract class AbstractSqlParametrized<T extends AbstractSqlParametrized<T>> {
  private static final String TRACE_QUERY_RESULT = "query.result.value.";
  private static final String TRACE_ERROR_TITLE = "error";
  private static final String TRACE_RESPONSE_QUANTITY = "response.quantity";
  private static final String TRACE_QUERY_PARAM = "query.param.";
  private static final String TRACE_QUERY_TITLE = "query.sql";

  private static class Prepared implements AutoCloseable {
    public final PreparedStatement stat;
    public final Map<Integer, SqlParameterValue> values;

    public Prepared(PreparedStatement stat, Map<Integer, SqlParameterValue> values) {
      super();
      this.stat = stat;
      this.values = values;
    }

    @Override
    public void close() throws SQLException {
      this.stat.close();
    }
  }
  private static class ChildRequest<R> {
    public final int batch;
    public final String name;
    // El campo del padre que extramos para la query
    public final String bind;
    // El campo del hijo que apunta al padre para asociarlo
    public final String ref;
    public final String query;
    public final SqlConverter<R> converter;
    // Calculated
    public final List<String> params = new ArrayList<>();
    // Calculated
    public final Map<String, CompletableFuture<R>> futures = new LinkedHashMap<>();
    public final Map<String, List<R>> childData = new LinkedHashMap<>();

    public ChildRequest(int batch, String name, String bind, String ref, String query,
        SqlConverter<R> converter) {
      this.batch = batch;
      this.name = name;
      this.bind = bind;
      this.ref = ref;
      this.query = query;
      this.converter = converter;
    }
  }

  private final Map<String, SqlParameterValue> parameters = new LinkedHashMap<>();
  private final Map<String, Integer> arrays = new LinkedHashMap<>();
  private final List<ChildRequest<?>> childs = new ArrayList<>();

  private final Connection connection;
  private final SqlTemplate template;

  /* default */ AbstractSqlParametrized(SqlTemplate template) {
    this.connection = template.currentConnection();
    this.template = template;
  }

  @SuppressWarnings("unchecked")
  protected T with(String name, SqlParameterValue consumer) {
    parameters.put(name, consumer);
    if (consumer instanceof SqlListParameterValue list) {
      arrays.put(name, list.size());
    }
    return (T) this;
  }

  protected Integer executeUpdate(String sql) {
    Optional<Span> createSpan = template.createSpan("execute update");
    try (Prepared prep = prepareStatement(sql); PreparedStatement run = prep.stat) {
      createSpan.ifPresent(span -> {
        span.setAttribute(TRACE_QUERY_TITLE, sql);
        prep.values
            .forEach((key, value) -> span.setAttribute(TRACE_QUERY_PARAM + key, value.toString()));
      });
      Integer result = run.executeUpdate();
      createSpan
          .ifPresent(span -> span.setAttribute(TRACE_RESPONSE_QUANTITY, String.valueOf(result)));
      return result;
    } catch (SQLException error) {
      createSpan.ifPresent(span -> {
        span.setAttribute(TRACE_ERROR_TITLE, true);
        span.recordException(error);
      });
      throw UncheckedSqlException.exception(connection, error);
    } catch (RuntimeException error) {
      createSpan.ifPresent(span -> {
        span.setAttribute(TRACE_ERROR_TITLE, true);
        span.recordException(error);
      });
      throw error;
    } finally {
      createSpan.ifPresent(Span::end);
    }
  }

  // consulta hija
  public <S> void child(int batch, String name, String sql, String parentBind, String childRef,
      SqlConverter<S> converter) {
    // select * from childs where bind in (?)
    childs.add(new ChildRequest<>(batch, name, parentBind, childRef, sql, converter));
  }


  protected <R> SqlResult<R> executeQuery(String sql, SqlConverter<R> converter) {
    return new SqlResult<R>() {
      @Override
      public Optional<R> one() {
        return queryExecutor(limitResults(sql, 1), converter).stream().findFirst();
      }

      @Override
      public List<R> limit(Optional<Integer> max) {
        return max.map(this::limit).orElseGet(this::all);
      }

      @Override
      public List<R> limit(int max) {
        return queryExecutor(limitResults(sql, max), converter);
      }

      @Override
      public List<R> all() {
        return queryExecutor(sql, converter);
      }
    };
  }

  private List<List<String>> partitionList(List<String> list, int windowSize) {
    return IntStream.range(0, (list.size() + windowSize - 1) / windowSize)
        .mapToObj(i -> list.subList(i * windowSize, Math.min((i + 1) * windowSize, list.size())))
        .toList();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void resolveChilds(ChildRequest child, Optional<Span> parent, List<String> offset)
      throws SQLException {
    Optional<Span> childSpan = template.createSpan("execute child query", parent);
    Map<String, List<Integer>> childParams = new LinkedHashMap<>();
    List<Integer> list = new ArrayList<>();
    list.add(1);
    childParams.put(child.ref, list);
    String theSql = formatSql(child.query, childParams, Map.of(child.ref, offset.size()));
    try (PreparedStatement childps = connection.prepareStatement(theSql)) {
      // Replace childs params
      Map<Integer, SqlParameterValue> applyParameters = applyParameters(childParams, childps,
          Map.of(child.ref, SqlListParameterValue.of(offset.toArray(new String[0]))));
      childSpan.ifPresent(span -> {
        span.setAttribute(TRACE_QUERY_TITLE, theSql);
        applyParameters
            .forEach((key, value) -> span.setAttribute(TRACE_QUERY_PARAM + key, value.toString()));
      });
      try (ResultSet childRes = childps.executeQuery()) {
        while (childRes.next()) {
          Optional<Object> ref =
              child.converter.convert(SqlResultSet.builder().set(childRes).build());
          if (ref.isPresent()) {
            String parentRef = childRes.getString(child.ref);
            ((List) child.childData.get(parentRef)).add(ref.get());
          }
        }
      }
      childSpan.ifPresent(span -> child.childData.forEach((key, value) -> {
        List values = (List) value;
        span.setAttribute("query.result." + key + ".count", values.size());
        for (int i = 0; i < values.size(); i++) {
          span.setAttribute("query.result." + key + ".value." + i, String.valueOf(values.get(i)));
        }
      }));
      offset.forEach(
          key -> ((CompletableFuture) child.futures.get(key)).complete(child.childData.get(key)));
    } catch (SQLException error) {
      childSpan.ifPresent(span -> {
        span.setAttribute(TRACE_ERROR_TITLE, true);
        span.recordException(error);
      });
      throw UncheckedSqlException.exception(connection, error);
    } catch (RuntimeException error) {
      childSpan.ifPresent(span -> {
        span.setAttribute(TRACE_ERROR_TITLE, true);
        span.recordException(error);
      });
      throw error;
    } finally {
      childSpan.ifPresent(Span::end);
    }
  }

  private Prepared prepareStatement(String sql) throws SQLException {
    Map<String, List<Integer>> parameterIndexMap = new LinkedHashMap<>();
    PreparedStatement prepareStatement =
        connection.prepareStatement(formatSql(sql, parameterIndexMap));
    Map<Integer, SqlParameterValue> applyParameters =
        applyParameters(parameterIndexMap, prepareStatement);
    return new Prepared(prepareStatement, applyParameters);
  }

  private String formatSql(String sql, Map<String, List<Integer>> parameterIndexMap)
      throws SQLException {
    return formatSql(sql, parameterIndexMap, arrays);

  }

  private String formatSql(String sql, Map<String, List<Integer>> parameterIndexMap,
      Map<String, Integer> larrays) throws SQLException {
    sql = parseSql(escapeIdentifiers(sql), parameterIndexMap);
    // aquellos que sean listas: toca expandirlos.
    List<Integer> listSizes = new ArrayList<>();
    larrays.forEach((name, value) -> {
      if (!parameterIndexMap.containsKey(name)) {
        throw new IllegalArgumentException("No param " + name + " on the sentence");
      }
      listSizes.add(value);
      List<Integer> positions = parameterIndexMap.get(name);
      for (Integer position : positions) {
        // Must sum offset to all positions greater than position
        parameterIndexMap.forEach((key, indexes) -> parameterIndexMap.put(key,
            indexes.stream().map(index -> index > position ? index + value - 1 : index).toList()));
      }
    });
    if (!listSizes.isEmpty()) {
      sql = replaceInPlaceholders(sql, listSizes);
    }
    return sql;
  }

  private String parseSql(String sql, Map<String, List<Integer>> parameterIndexMap) {
    StringBuilder parsedSql = new StringBuilder();
    int index = 1;

    for (int i = 0; i < sql.length(); i++) {
      char c = sql.charAt(i);
      if (c == ':' && i + 1 < sql.length()
          && (Character.isLetter(sql.charAt(i + 1)) || sql.charAt(i + 1) == '_')) {
        int j = i + 1;
        while (j < sql.length()
            && (Character.isLetterOrDigit(sql.charAt(j)) || sql.charAt(j) == '_')) {
          j++;
        }
        String paramName = sql.substring(i + 1, j);
        if (!parameterIndexMap.containsKey(paramName)) {
          parameterIndexMap.put(paramName, new ArrayList<>());
        }
        parameterIndexMap.get(paramName).add(index++);
        parsedSql.append('?');
        i = j - 1;
      } else {
        parsedSql.append(c);
      }
    }
    return parsedSql.toString();
  }

  private String replaceInPlaceholders(String sql, List<Integer> paramSizes) {
    // Patrón para localizar IN (?)
    Pattern pattern = Pattern.compile("(?<!')\\b[Ii][Nn]\\s*\\(\\s*\\?\\s*\\)(?!')");
    Matcher matcher = pattern.matcher(sql);

    StringBuffer result = new StringBuffer();
    int index = 0;

    while (matcher.find()) {
      if (index >= paramSizes.size()) {
        throw new IllegalArgumentException("Insufficient sizes provided for IN placeholders.");
      }

      if (0 == paramSizes.get(index)) {
        // Si no hay parametros, entonces será siempre nulo.
        matcher.appendReplacement(result, "IN (NULL)");
      } else {
        // Generar el reemplazo dinámico con ? según el tamaño del parámetro
        String placeholders = String.join(", ", "?".repeat(paramSizes.get(index)).split(""));
        matcher.appendReplacement(result, "IN (" + placeholders + ")");
      }
      index++;
    }
    matcher.appendTail(result);
    return result.toString();
  }

  private String escapeIdentifiers(String sql) throws SQLException {
    DatabaseMetaData metaData = connection.getMetaData();
    String escape = metaData.getIdentifierQuoteString();
    Matcher matcher = Pattern.compile("'([^']*)'|\"([^\"]*)\"").matcher(sql);
    StringBuilder result = new StringBuilder();
    int lastIndex = 0;

    while (matcher.find()) {
      result.append(sql, lastIndex, matcher.start());
      if (matcher.group(1) != null) {
        result.append(matcher.group());
      } else {
        result.append(escape).append(matcher.group().replace("\"", "")).append(escape);
      }
      lastIndex = matcher.end();
    }
    result.append(sql, lastIndex, sql.length());
    return result.toString();
  }

  private Map<Integer, SqlParameterValue> applyParameters(
      Map<String, List<Integer>> parameterIndexMap, PreparedStatement preparedStatement)
      throws SQLException {
    return applyParameters(parameterIndexMap, preparedStatement, parameters);
  }

  private Map<Integer, SqlParameterValue> applyParameters(
      Map<String, List<Integer>> parameterIndexMap, PreparedStatement preparedStatement,
      Map<String, SqlParameterValue> lparameters) throws SQLException {
    Map<Integer, SqlParameterValue> assigned = new HashMap<>();
    for (Entry<String, SqlParameterValue> entry : lparameters.entrySet()) {
      String key = entry.getKey();
      SqlParameterValue value = entry.getValue();
      if (!parameterIndexMap.containsKey(key)) {
        throw new IllegalArgumentException("No param " + key + " on the sentence");
      } else {
        for (Integer index : parameterIndexMap.get(key)) {
          assigned.put(index, value);
          value.accept(index, preparedStatement);
        }
      }
    }
    return assigned;
  }

  private String limitResults(String query, int size) {
    return query + " limit " + size;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private <R> List<R> queryExecutor(String query, SqlConverter<R> converter) {
    for (ChildRequest child : childs) {
      child.futures.clear();
      child.childData.clear();
    }
    Optional<Span> querySpan = template.createSpan("execute query");
    try (Prepared prep = prepareStatement(query);
        PreparedStatement prepareStatement = prep.stat;
        ResultSet executeQuery = prepareStatement.executeQuery()) {
      querySpan.ifPresent(span -> {
        span.setAttribute(TRACE_QUERY_TITLE, query);
        prep.values
            .forEach((key, value) -> span.setAttribute(TRACE_QUERY_PARAM + key, value.toString()));
      });
      List<R> data = new ArrayList<>();
      // Si tengo listas hijas => tengo que ir por ellas.
      while (executeQuery.next()) {
        fillRow(executeQuery, converter).ifPresent(data::add);
      }
      querySpan.ifPresent(span -> {
        span.setAttribute(TRACE_RESPONSE_QUANTITY, data.size());
        for (int i = 0; i < data.size(); i++) {
          span.setAttribute(TRACE_QUERY_RESULT + i, String.valueOf(data.get(i)));
        }
      });
      for (ChildRequest child : childs) {
        List<List<String>> partitionList = partitionList(child.params, child.batch);
        for (List<String> list : partitionList) {
          resolveChilds(child, querySpan, list);
        }
      }
      return data;
    } catch (SQLException error) {
      querySpan.ifPresent(span -> {
        span.setAttribute(TRACE_ERROR_TITLE, true);
        span.recordException(error);
      });
      throw UncheckedSqlException.exception(connection, error);
    } catch (RuntimeException error) {
      querySpan.ifPresent(span -> {
        span.setAttribute(TRACE_ERROR_TITLE, true);
        span.recordException(error);
      });
      throw error;
    } finally {
      querySpan.ifPresent(Span::end);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private <R> Optional<R> fillRow(ResultSet executeQuery, SqlConverter<R> converter)
      throws SQLException {
    // foreach child
    Map<String, CompletableFuture<?>> childData = new HashMap<>();
    for (ChildRequest child : childs) {
      String of = executeQuery.getString(child.bind);
      child.params.add(of);
      CompletableFuture<?> future = new CompletableFuture<>();
      child.futures.put(of, future);
      child.childData.put(of, new ArrayList<>());
      childData.put(child.name, future);
    }
    SqlResultSet row = SqlResultSet.builder().set(executeQuery).childs(childData).build();
    return converter.convert(row);
  }
}

