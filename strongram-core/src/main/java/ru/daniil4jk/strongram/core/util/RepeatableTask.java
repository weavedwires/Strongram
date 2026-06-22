package ru.daniil4jk.strongram.core.util;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A cancellable repeating task scheduled at a fixed rate via a
 * {@link java.util.concurrent.ScheduledExecutorService}. Implements {@link AutoCloseable}
 * to allow use in try-with-resources blocks.
 */
public class RepeatableTask implements AutoCloseable {
    private final ScheduledExecutorService scheduledExecutor;
    private final Duration betweenCalling;
    private final Runnable action;
    private ScheduledFuture<?> task;

    public RepeatableTask(Runnable action, Duration betweenCalling, ScheduledExecutorService scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
        this.betweenCalling = betweenCalling;
        this.action = action;
    }

    public void start() {
        task = scheduledExecutor.scheduleAtFixedRate(
                action,
                0,
                betweenCalling.getNano(),
                TimeUnit.NANOSECONDS
        );
    }

    @Override
    public void close() {
        boolean successfully = task.cancel(false);
        if (!successfully) {
            throw new IllegalStateException("RepeatableTask not stopped successfully");
        }
        action.run();
    }
}