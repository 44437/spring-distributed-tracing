package com.u44437.back_service.controller;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class BackServiceController {
  private final OpenTelemetry openTelemetry;
  private final Tracer tracer;

  public BackServiceController(OpenTelemetry openTelemetry, Tracer tracer) {
    this.openTelemetry = openTelemetry;
    this.tracer = tracer;
  }

  private class OtelMapGetter implements TextMapGetter<Map<String, String>> {
    @Override
    public String get(Map<String, String> carrier, String key) {
      return carrier.get(key);
    }

    @Override
    public Iterable<String> keys(Map<String, String> carrier) {
      return carrier.keySet();
    }
  }

  @GetMapping("/")
  public ResponseEntity getSomething(@RequestHeader Map<String, String> headers) throws InterruptedException {
    Context context = openTelemetry.getPropagators().getTextMapPropagator().extract(Context.current(), headers, new OtelMapGetter());

    Span span = tracer.spanBuilder("controller-getSomething").setParent(context).startSpan();

    Thread.sleep(300);

    span.end();

    return ResponseEntity.ok().build();
  }
}
