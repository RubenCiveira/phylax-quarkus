package net.civeira.phylax.common.infrastructure.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Builder class for constructing conditional SQL WHERE clauses in a composable, hierarchical and
 * functional way.
 * <p>
 * This class supports logical combinations (AND, OR, NOT) of individual conditions or nested
 * {@code PartialWhere} blocks, and integrates with the {@link SchematicQuery} to support
 * parameterized SQL.
 * </p>
 */
public class PartialWhere {

  /**
   * Represents a single node in a WHERE clause tree.
   *
   * <p>
   * A condition is either a {@link LeafCondition} (a direct field comparison with a bound
   * parameter) or a {@link GroupCondition} (a nested {@link PartialWhere} sub-tree combined with
   * its own logic operator).
   * </p>
   */
  sealed
  interface Condition
  permits LeafCondition, GroupCondition
  {
    }

  /**
   * A leaf condition comparing a single field against a bound parameter.
   *
   * @param tableAlias optional alias or table name prefix; may be {@code null}
   * @param field      the column name to compare
   * @param operator   the SQL comparison operator
   * @param value      the parameter value to bind
   */
  record LeafCondition(String tableAlias, String field, SqlOperator operator,
      SqlParameterValue value) implements Condition {}

  /**
   * A group condition wrapping a nested {@link PartialWhere} sub-tree.
   *
   * @param nested the nested partial WHERE to embed in the clause
   */
  record GroupCondition(PartialWhere nested) implements Condition {}

    /**
     * Combines multiple {@code PartialWhere} instances using logical OR.
     *
     * @param partials one or more {@code PartialWhere} instances
     * @return a new {@code PartialWhere} representing the OR of the inputs
     */
    public static PartialWhere or(PartialWhere... partials) {
      PartialWhere partial = new PartialWhere("or", "");
      for (PartialWhere part : partials) {
        if (!part.isEmpty()) {
          partial.conditions.add(new GroupCondition(part));
        }
      }
      return partial;
    }

    /**
     * Combines multiple optional {@code PartialWhere} instances using logical OR, ignoring empty or
     * absent values.
     *
     * @param partials optional {@code PartialWhere} instances
     * @return a new {@code PartialWhere} representing the OR of the inputs
     */
    @SafeVarargs
    public static PartialWhere or(Optional<PartialWhere>... partials) {
      PartialWhere partial = new PartialWhere("or", "");
      for (Optional<PartialWhere> part : partials) {
        part.ifPresent(tpart -> {
          if (!tpart.isEmpty()) {
            partial.conditions.add(new GroupCondition(tpart));
          }
        });
      }
      return partial;
    }

    /**
     * Negates a given {@code PartialWhere} using a logical NOT.
     *
     * @param partial the {@code PartialWhere} to negate
     * @return a new {@code PartialWhere} with a NOT prefix
     */
    public static PartialWhere not(PartialWhere partial) {
      PartialWhere negated = new PartialWhere("and", "not");
      if (!partial.isEmpty()) {
        negated.conditions.add(new GroupCondition(partial));
      }
      return negated;
    }

    /**
     * Negates a given optional {@code PartialWhere}, if present and not empty.
     *
     * @param partial optional {@code PartialWhere} to negate
     * @return a new {@code PartialWhere} with a NOT prefix
     */
    public static PartialWhere not(Optional<PartialWhere> partial) {
      PartialWhere negated = new PartialWhere("and", "not");
      partial.ifPresent(tpartial -> {
        if (!tpartial.isEmpty()) {
          negated.conditions.add(new GroupCondition(tpartial));
        }
      });
      return negated;
    }

    /**
     * Combines multiple {@code PartialWhere} instances using logical AND.
     *
     * @param partials one or more {@code PartialWhere} instances
     * @return a new {@code PartialWhere} representing the AND of the inputs
     */
    public static PartialWhere and(PartialWhere... partials) {
      PartialWhere partial = new PartialWhere("and", "");
      for (PartialWhere part : partials) {
        if (!part.isEmpty()) {
          partial.conditions.add(new GroupCondition(part));
        }
      }
      return partial;
    }

    /**
     * Combines multiple optional {@code PartialWhere} instances using logical AND, ignoring empty
     * or absent values.
     *
     * @param partials optional {@code PartialWhere} instances
     * @return a new {@code PartialWhere} representing the AND of the inputs
     */
    @SafeVarargs
    public static PartialWhere and(Optional<PartialWhere>... partials) {
      PartialWhere partial = new PartialWhere("and", "");
      for (Optional<PartialWhere> part : partials) {
        part.ifPresent(tpart -> {
          if (!tpart.isEmpty()) {
            partial.conditions.add(new GroupCondition(tpart));
          }
        });
      }
      return partial;
    }

    /**
     * Creates a single condition with an ON-clause (typically used in JOINs).
     *
     * @param on the optional alias/table name
     * @param field the field name to compare
     * @param operator the SQL comparison operator
     * @param value the parameter value to bind
     * @return a {@code PartialWhere} containing this single condition
     */
    public static PartialWhere where(String on, String field, SqlOperator operator,
        SqlParameterValue value) {
      PartialWhere partial = new PartialWhere("and", "");
      partial.conditions.add(new LeafCondition(on, field, operator, value));
      return partial;
    }

    /**
     * Creates a single condition without an ON-clause.
     *
     * @param field the field name
     * @param operator the SQL comparison operator
     * @param value the parameter value
     * @return a {@code PartialWhere} containing this condition
     */
    public static PartialWhere where(String field, SqlOperator operator, SqlParameterValue value) {
      PartialWhere partial = new PartialWhere("and", "");
      partial.conditions.add(new LeafCondition(null, field, operator, value));
      return partial;
    }

    /**
     * Returns an empty {@code PartialWhere} that has no conditions.
     *
     * @return an empty {@code PartialWhere}
     */
    public static PartialWhere empty() {
      return new PartialWhere("", "");
    }

    /** Logical join type: "and" or "or". */
    private final String join;

    /** Logical prefix or modifier: "", "not", etc. */
    private final String converter;

    /** Internal list of typed conditions in this WHERE node. */
    /* default */ List<Condition> conditions = new ArrayList<>();

  /**
   * Constructs a new {@code PartialWhere} with join type and converter.
   *
   * @param join the join keyword ("and", "or")
   * @param converter a prefix modifier such as "not"
   */
  private PartialWhere(String join, String converter) {
    this.join = join;
    this.converter = converter;
  }

  /**
   * Checks whether this {@code PartialWhere} contains no conditions.
   *
   * @return {@code true} if empty; {@code false} otherwise
   */
  /* default */ boolean isEmpty() {
    return conditions.isEmpty();
  }

  /**
   * Builds the SQL WHERE clause recursively, registering parameters on the provided
   * {@link SchematicQuery}.
   *
   * @param prefix a prefix index used to generate unique parameter names
   * @param parametrized the parent {@link SchematicQuery} that collects parameters
   * @return a SQL WHERE string representation of this partial condition
   */
  /* default */ String append(int prefix, SchematicQuery parametrized) {
    List<String> wheres = new ArrayList<>();
    for (Condition condition : conditions) {
      switch (condition) {
        case GroupCondition gc -> {
          if (!gc.nested().isEmpty()) {
            String where = gc.nested().append(++prefix, parametrized);
            prefix += where.length();
            wheres.add(where);
          }
        }
        case LeafCondition lc -> {
          String name = "_field_" + (++prefix);
          parametrized.getParametrized().with(name, lc.value());
          wheres.add((null == lc.tableAlias() ? "" : parametrized.escape(lc.tableAlias()) + ".")
              + parametrized.escape(lc.field()) + " " + lc.operator().value + " :" + name);
        }
      }
    }
    if (wheres.isEmpty()) {
      return "";
    } else if (wheres.size() == 1) {
      return (converter + " " + wheres.get(0)).trim();
    } else {
      return converter + "(" + String.join(" " + join + " ", wheres) + ")";
    }
  }
}
