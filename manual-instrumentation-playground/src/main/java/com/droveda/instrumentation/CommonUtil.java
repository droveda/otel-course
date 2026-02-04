package com.droveda.instrumentation;

import java.time.Duration;

public class CommonUtil {

    public static void sleepSeconds(int seconds) {
        sleep(Duration.ofSeconds(seconds));
    }

    public static void sleepMillis(int milliseconds) {
        sleep(Duration.ofMillis(milliseconds));
    }

    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
