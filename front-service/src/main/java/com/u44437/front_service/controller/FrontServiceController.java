package com.u44437.front_service.controller;

import com.u44437.front_service.repository.FrontServiceRepository;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.u44437.front_service.util.Constant.LAYER;

@RestController
@RequestMapping
public class FrontServiceController {
  private final FrontServiceRepository frontServiceRepository;
  private final Tracer tracer;
  private static final String LAYER_CONTROLLER = "controller";

  public FrontServiceController(FrontServiceRepository frontServiceRepository, Tracer tracer) {
    this.frontServiceRepository = frontServiceRepository;
    this.tracer = tracer;
  }

  @GetMapping("/")
  public ResponseEntity getSomething() throws Exception {
    Span span = tracer.spanBuilder("controller-getSomething")
            .setAttribute(LAYER, LAYER_CONTROLLER)
            .setSpanKind(SpanKind.SERVER)
            .startSpan();

    Thread.sleep(300);

    frontServiceRepository.getSomething(Context.current().with(span));

    span.end();

    return ResponseEntity.ok().build();
  }
}
