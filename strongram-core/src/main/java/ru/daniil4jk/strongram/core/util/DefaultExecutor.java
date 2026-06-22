package ru.daniil4jk.strongram.core.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Holder for a shared {@link java.util.concurrent.ScheduledExecutorService} that is
 * lazily created on first access. Used as the default executor by Strongram components
 * when no explicit executor is provided.
 */
@Slf4j
public class DefaultExecutor {
    private static final Lazy<ScheduledExecutorService> DEFAULT_EXECUTOR = new Lazy<>(
            DefaultExecutor::createScheduledCashedThreadPool
    );

    private static @NotNull ScheduledThreadPoolExecutor createScheduledCashedThreadPool() {
        var exec = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
        exec.setKeepAliveTime(60, TimeUnit.SECONDS);
        return exec;
    }

    public static ScheduledExecutorService initOrGet(String callerName) {
        if (!isInitialized()) {
            log.info("Creating DefaultExecutor thread pool. " +
                    "It is used when no explicit Executor is specified in some of Strongram components. " +
                    "Now DefaultExecutor called by {}", callerName);
        }
        return DEFAULT_EXECUTOR.initOrGet();
    }

    public static boolean isInitialized() {
        return DEFAULT_EXECUTOR.isInitialized();
    }
}
