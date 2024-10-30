package com.u44437.back_service.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ServiceAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OpenTelemetryConfiguration {
  @Value("${otel.exporter.url}")
  private String exporterUrl;

  @Value("${otel.service.name}")
  private String serviceName;

  @Value("${otel.tracer.scope.name}")
  private String tracerScopeName;

  @Bean
  public OpenTelemetry openTelemetry() {
    OtlpHttpSpanExporter otlpHttpSpanExporter =
            OtlpHttpSpanExporter.builder()
                    .setEndpoint(exporterUrl)
                    .setTimeout(Duration.ofSeconds(3))
                    .build();

    Resource resource =
            Resource.create(Attributes.of(
                    ServiceAttributes.SERVICE_NAME, serviceName)
            );

    SdkTracerProvider sdkTracerProvider =
            SdkTracerProvider.builder()
                    .addSpanProcessor(BatchSpanProcessor.builder(otlpHttpSpanExporter).build())
                    .setResource(Resource.getDefault().merge(resource))
                    .build();

    OpenTelemetrySdk openTelemetrySdk =
            OpenTelemetrySdk.builder()
                    .setTracerProvider(sdkTracerProvider)
                    .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                    .build();

    Runtime.getRuntime().addShutdownHook(new Thread(openTelemetrySdk::close));

    return openTelemetrySdk;
  }

  @Bean
  public Tracer tracer(OpenTelemetry openTelemetry){
    return openTelemetry.getTracer(tracerScopeName);
  }
}
