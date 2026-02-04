package com.droveda.instrumentation;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

public class OpenTelemetryConfig {

    private static final String COLLECTOR_ENDPOINT = "http://localhost:4317";
    private static final OpenTelemetry openTelemetry = initOpenTelemetry();

    private static OpenTelemetry initOpenTelemetry() {
        return OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider())
                .build();
    }

    private static SdkTracerProvider tracerProvider() {
        var exporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint(COLLECTOR_ENDPOINT)
                .build();

        var processor = SimpleSpanProcessor.builder(exporter).build(); // This is for local testing
//        var processor = BatchSpanProcessor.builder(exporter).build(); //this is for production applications

        return SdkTracerProvider.builder()
                .setResource(resource())
                .addSpanProcessor(processor)
                .build();
    }

    private static Resource resource() {
        return Resource.create(Attributes.of(
                AttributeKey.stringKey("service.name"), "order-service"
        ));
    }

    public static Tracer tracer(Class<?> type) {
        return openTelemetry.getTracer(type.getName());
    }

}
