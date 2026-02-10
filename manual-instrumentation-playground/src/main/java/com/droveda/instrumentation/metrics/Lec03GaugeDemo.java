package com.droveda.instrumentation.metrics;

import com.droveda.instrumentation.CommonUtil;
import com.droveda.instrumentation.OpenTelemetryConfig;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableLongGauge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Lec03GaugeDemo {

    private static final Logger log = LoggerFactory.getLogger(Lec03GaugeDemo.class);
    private static final Meter meter = OpenTelemetryConfig.meter(Lec03GaugeDemo.class);

    public static void main(String[] args) {
        try (var gauge = createJvmMemoryUsedGauge()) {
            simulateMemoryUsage();
        }
    }

    private static void simulateMemoryUsage() {
        var memory = new ArrayList<byte[]>();

        for (int i = 1; i <= 10_000; i++) {
            // allocate ~10MB each iteration
            memory.add(new byte[1024 * 1024 * 10]);
            log.info("allocated {} MB memory", memory.size() * 10);

            CommonUtil.sleepSeconds(1);

            //clear memory every minute to see the gauge drop
            if (i % 60 == 0) {
                log.info("clearing memory");
                memory.clear();
                System.gc();
            }
        }

    }

    private static ObservableLongGauge createJvmMemoryUsedGauge() {
        return meter.gaugeBuilder("jvm.memory.used")
                .ofLongs()
                .setDescription("The amount of JVM memory currently used")
                .setUnit("By")
                .buildWithCallback(o -> {
                    log.info("executing callback for jvm memory used");
                    var usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    o.record(usedMemory);
                });
    }

}
