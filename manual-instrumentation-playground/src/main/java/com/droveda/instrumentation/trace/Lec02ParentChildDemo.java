package com.droveda.instrumentation.trace;

import com.droveda.instrumentation.CommonUtil;
import com.droveda.instrumentation.OpenTelemetryConfig;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;

public class Lec02ParentChildDemo {

    private static final Tracer tracer = OpenTelemetryConfig.tracer(Lec02ParentChildDemo.class);

    public static void main(String[] args) {
        var demo = new Lec02ParentChildDemo();
        //POST /orders
        demo.processOrder();

        CommonUtil.sleepSeconds(2);
    }


    private void processOrder() {
        var span = tracer.spanBuilder("processOrder").startSpan();

        try {
            processPayment();
            deductInventory();
            sendNotification();

            span.setAttribute("order.id", 123);
            span.setAttribute("order.amount", 1000);

            span.setStatus(StatusCode.OK);
        } catch (Exception ex) {
            span.recordException(ex);
            span.setStatus(StatusCode.ERROR, ex.getLocalizedMessage());
        } finally {
            span.end();
        }
    }

    private void processPayment() {
        var span = tracer.spanBuilder("processPayment").startSpan();

        try {
            CommonUtil.sleepMillis(150);

            span.setAttribute("payment.method", "CREDIT_CARD");
            span.setStatus(StatusCode.OK);
        } catch (Exception ex) {
            span.recordException(ex);
            span.setStatus(StatusCode.ERROR, ex.getLocalizedMessage());
        } finally {
            span.end();
        }
    }

    private void deductInventory() {
        CommonUtil.sleepMillis(125);

    }

    private void sendNotification() {
        CommonUtil.sleepMillis(100);
//        throw new RuntimeException("IO Error");
    }


}
