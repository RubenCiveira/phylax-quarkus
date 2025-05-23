/* @autogenerated */
package net.civeira.phylax.common.infrastructure.connector.quarkus.munity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.ext.web.client.HttpRequest;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.uritemplate.UriTemplate;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.common.infrastructure.connector.RemoteConnection;
import net.civeira.phylax.common.infrastructure.connector.RemoteQuery;

@Slf4j
public class MunityWebQuery implements RemoteQuery {
  public static enum Method {
    POST, PUT, PATCH, DELETE
  }

  public static MunityWebQuery create(Tracer tracer, WebClient client, Method method, String target,
      Object body) {
    HttpRequest<?> query;
    try {
      URI url = new URI(target.replace("{", "").replace("}", ""));
      String template = url.getPath();
      if (null != url.getRawUserInfo())
        template += "?" + url.getRawQuery();
      query = createConn(client, method, template, body).port(url.getPort()).host(url.getHost());
    } catch (URISyntaxException e) {
      log.warn("Unable to parte {} as url", target);
      query = createConn(client, method, target, body);
    }
    return new MunityWebQuery(query, body, tracer);
  }

  private static HttpRequest<?> createConn(WebClient client, Method method, String target,
      Object body) {
    HttpRequest<?> query;
    if (method == Method.POST) {
      query = client.post(UriTemplate.of(target));
    } else if (method == Method.PUT) {
      query = client.put(UriTemplate.of(target));
    } else if (method == Method.PATCH) {
      query = client.patch(UriTemplate.of(target));
    } else if (method == Method.DELETE) {
      query = client.delete(UriTemplate.of(target));
    } else {
      query = client.get(UriTemplate.of(target));;
    }
    if (null == body) {
      query = query.putHeader("Content-Type", MediaType.APPLICATION_JSON);
    }
    query = query.putHeader("Accept", MediaType.APPLICATION_JSON);
    return query;
  }

  private final HttpRequest<?> client;
  private final Object body;
  private final Tracer tracer;

  private MunityWebQuery(HttpRequest<?> client, Object body, Tracer tracer) {
    this.body = body;
    this.client = client;
    this.tracer = tracer;
  }

  @Override
  public RemoteQuery queryParam(Map<String, String> params) {
    params.forEach(client::addQueryParam);
    return this;
  }

  @Override
  public RemoteQuery queryParam(String name, String value) {
    client.addQueryParam(name, value);
    return this;
  }

  @Override
  public RemoteQuery pathParam(Map<String, String> params) {
    params.forEach(client::setTemplateParam);
    return this;
  }

  @Override
  public RemoteQuery pathParam(String name, String value) {
    client.setTemplateParam(name, value);
    return this;
  }

  @Override
  public RemoteQuery header(String name, String value) {
    client.putHeader(name, value);
    return this;
  }

  @Override
  public RemoteQuery header(String name, List<String> values) {
    client.putHeader(name, values);
    return this;
  }

  @Override
  public RemoteQuery headers(Map<String, List<String>> headers) {
    headers.forEach(this::header);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> RemoteConnection processor(Class<T> type, Consumer<T> consumer) {
    if (type.isAssignableFrom(String.class)) {
      client.putHeader("Accept", MediaType.TEXT_PLAIN);
    }
    JsonObject bodyObject = null == body ? null : JsonObject.mapFrom(body);
    var uni = (null == body ? client.send() : client.sendJsonObject(bodyObject));
    if (null != tracer) {
      Span parentSpan = Span.current();
      if (parentSpan.getSpanContext().isValid()) {
        Span span = tracer.spanBuilder("http-request").setParent(Context.current().with(parentSpan))
            .setSpanKind(SpanKind.CLIENT).startSpan();

        uni = uni.onItem().invoke(response -> {
          Map<String, String> requestHeaders = client.headers().entries().stream()
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
          Map<String, String> responseHeaders = response.headers().entries().stream()
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
          // Agregar atributos al Span con la información de la petición
          span.setAttribute("http.method", client.method().name());
          span.setAttribute("http.url", client.uri());
          span.setAttribute("http.request.headers", requestHeaders.toString());
          if (null != bodyObject) {
            span.setAttribute("http.request.body", bodyObject.encode());
          }
          span.setAttribute("http.status_code", response.statusCode());
          span.setAttribute("http.response.body", response.bodyAsString());
          span.setAttribute("http.response.headers", responseHeaders.toString());
          span.setAttribute("error", response.statusCode() >= 400);
        }).onFailure().invoke(error -> {
          // En caso de error, registrar el fallo en el Span
          span.setAttribute("error", true);
          span.recordException(error);
        }).onTermination().invoke(span::end);
      }
    }
    return new MunityWebConnection(uni.onItem().transform(item -> {
      if (type.isAssignableFrom(String.class)) {
        consumer.accept((T) item.bodyAsString());
      } else {
        consumer.accept((T) item.bodyAsJson(type));
      }
      return "";
    }));
  }

  @Override
  public RemoteConnection processor(Runnable runnable) {
    return new MunityWebConnection(
        (null == body ? client.send() : client.sendJson(body)).onItem().transform(item -> {
          runnable.run();
          return "";
        }));
  }
}
