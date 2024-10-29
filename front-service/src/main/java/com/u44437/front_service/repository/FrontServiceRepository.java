package com.u44437.front_service.repository;

import com.u44437.front_service.client.BackServiceClient;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.u44437.front_service.util.Constant.LAYER;

@Repository
public class FrontServiceRepository {
  private final BackServiceClient backServiceClient;
  private final Tracer tracer;
  private final OpenTelemetry openTelemetry;
  private static final String LAYER_REPOSITORY = "repository";

  public FrontServiceRepository(
          BackServiceClient backServiceClient,
          Tracer tracer,
          OpenTelemetry openTelemetry) {
    this.backServiceClient = backServiceClient;
    this.tracer = tracer;
    this.openTelemetry = openTelemetry;
  }

  public void getSomething(Context context) throws InterruptedException {
    Span span = tracer.spanBuilder("repository-getSomething")
            .setParent(context)
            .setAttribute(LAYER, LAYER_REPOSITORY)
            .setSpanKind(SpanKind.CLIENT)
            .startSpan();

    Thread.sleep(200);

    CompletableFuture<Void> task1 = CompletableFuture.runAsync(
            () -> {
              Span _span = null;
              try {
                _span = tracer.spanBuilder("thread-1")
                        .setParent(context.with(span))
                        .setAttribute(LAYER, LAYER_REPOSITORY)
                        .setSpanKind(SpanKind.INTERNAL)
                        .startSpan();

                Thread.sleep(300);

                throw new InterruptedException("We've had a problem");
              } catch (InterruptedException e) {
                _span.setStatus(StatusCode.ERROR).addEvent(e.toString());
              } finally {
                _span.end();
              }
            });

    CompletableFuture<Void> task2 = CompletableFuture.runAsync(
            () -> {
              Span _span = null;
              try {
                _span = tracer.spanBuilder("thread-2")
                        .setParent(context.with(span))
                        .setAttribute(LAYER, LAYER_REPOSITORY)
                        .setSpanKind(SpanKind.CLIENT)
                        .startSpan();

                Map<String, String> headers = new HashMap<>();

                openTelemetry.getPropagators().getTextMapPropagator().inject(context.with(_span), headers,
                        (carrier, key, value) -> carrier.put(key, value));

                backServiceClient.getSomething(headers);

                Thread.sleep(200);
              } catch (InterruptedException e) {
                _span.setStatus(StatusCode.ERROR).addEvent(e.toString());
              } finally {
                _span.end();
              }
            });

    CompletableFuture.allOf(task1, task2).join();

    span.end();
  }
}
