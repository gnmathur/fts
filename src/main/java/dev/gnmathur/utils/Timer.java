package dev.gnmathur.utils;

import org.slf4j.Logger;

import java.util.concurrent.Callable;

public class Timer {
    public static <T> T time(String operation, Callable<T> task, Logger logger) {
        long start = System.nanoTime();
        try {
            T result = task.call();
            logger.info("{}: {}ms", operation, (System.nanoTime() - start) / 1_000_000);
            return result;
        } catch (Exception e) {
            logger.error("Error during {}: {}", operation, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
