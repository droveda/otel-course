package com.droveda.instrumentation.logs;

import com.droveda.instrumentation.CommonUtil;
import com.droveda.instrumentation.OpenTelemetryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

public class Lec01MDCDemo {

    private static final Logger log = LoggerFactory.getLogger(Lec01MDCDemo.class);
    private static final PaymentService paymentService = new PaymentService();

    public static void main(String[] args) throws InterruptedException {
        OpenTelemetryConfig.setupLoggingAppender();

        var demo = new Lec01MDCDemo();
//        demo.simpleLog();
//        demo.withSingleKey();
//        demo.withMultipleKeys();
//        demo.withMap();
//        demo.withAutoCloseable();
        demo.withContextPropagation();

        CommonUtil.sleepSeconds(1);
    }

    private void simpleLog() {
        paymentService.processPayment(100);
    }

    private void withSingleKey() {
        MDC.put("userId", "1");
        paymentService.processPayment(100);
        MDC.clear();
    }

    private void withMultipleKeys() {
        MDC.put("userId", "2");
        MDC.put("requestId", "123");
        paymentService.processPayment(100);
        MDC.clear();
    }


    private void withMap() {
        var map = Map.of("userId", "3");

        MDC.setContextMap(map);
        paymentService.processPayment(100);
        MDC.clear();
    }

    private void withAutoCloseable() {
        try (var a = MDC.putCloseable("userId", "4")) {
            paymentService.processPayment(100);
        }
        log.info("after auto closeable");
    }

    // with context propagation
    private void withContextPropagation() throws InterruptedException {
        MDC.put("userId", "5");
        var ctx = MDC.getCopyOfContextMap();
        var thread = Thread.ofVirtual().start(() -> {
            MDC.setContextMap(ctx);
            paymentService.processPayment(100);
            MDC.clear();
        });
        thread.join();
        MDC.clear();
    }

}
