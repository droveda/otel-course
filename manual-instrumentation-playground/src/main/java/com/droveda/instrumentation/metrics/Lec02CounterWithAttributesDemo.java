package com.droveda.instrumentation.metrics;

import com.droveda.instrumentation.CommonUtil;
import com.droveda.instrumentation.OpenTelemetryConfig;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;

import java.util.concurrent.ThreadLocalRandom;

public class Lec02CounterWithAttributesDemo {

    private static final Meter meter = OpenTelemetryConfig.meter(Lec02CounterWithAttributesDemo.class);

    public static void main(String[] args) {

        var counter = createProductViewCounter();
        ProductViewRecorder productViewRecorder = new ProductViewRecorder(counter);

        var controller = new ProductController(productViewRecorder);

        for (int i = 0; i < 10_000; i++) {
            controller.viewProduct(ThreadLocalRandom.current().nextInt(1, 4));
        }
    }

    // spring bean - thread safe
    private static LongCounter createProductViewCounter() {
        return meter.counterBuilder("app.product.view.count")
                //.ofDoubles()
                .setDescription("Total number of product view")
                .setUnit("{view}")
                .build();
    }

    private static class ProductController {
        private final ProductViewRecorder recorder;

        public ProductController(ProductViewRecorder recorder) {
            this.recorder = recorder;
        }

        // GET /product/{id}
        public void viewProduct(long id) {
            CommonUtil.sleepSeconds(1);
            this.recorder.recordView(id);
        }

    }

    // spring bean
    private static class ProductViewRecorder {
        private static final AttributeKey<Long> PRODUCT_ID_KEY = AttributeKey.longKey("product.id");
        private final LongCounter counter;

        public ProductViewRecorder(LongCounter counter) {
            this.counter = counter;
        }

        public void recordView(long productId) {
            this.counter.add(1, Attributes.of(
                    PRODUCT_ID_KEY, productId
            ));
        }
    }

}
