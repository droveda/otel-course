package com.droveda.instrumentation.trace;

import com.droveda.instrumentation.OpenTelemetryConfig;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;

import java.util.function.Consumer;
import java.util.function.Function;

public class TraceUtil {

    private static final Tracer tracer = OpenTelemetryConfig.tracer(TraceUtil.class);

    public static void trace(String spanName, Consumer<Span> consumer) {
        trace(spanName, span -> {
            consumer.accept(span);
            return null;
        });
    }

    public static <T> T trace(String spanName, Function<Span, T> function) {
        var span = tracer.spanBuilder(spanName).startSpan();

        try (var scope = span.makeCurrent()) {

            var t = function.apply(span);

            span.setStatus(StatusCode.OK);

            return t;
        } catch (Exception ex) {
            span.recordException(ex);
            span.setStatus(StatusCode.ERROR, ex.getLocalizedMessage());
            throw ex;
        } finally {
            span.end();
        }
    }

}
