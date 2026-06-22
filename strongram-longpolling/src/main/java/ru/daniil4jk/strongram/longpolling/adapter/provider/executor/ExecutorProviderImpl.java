package ru.daniil4jk.strongram.longpolling.adapter.provider.executor;

import ru.daniil4jk.strongram.core.util.Lazy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Default implementation of {@link ExecutorProvider}. Uses a lazily-initialized
 * single-thread executor that can be replaced via {@link #setExecutor}.
 */
public class ExecutorProviderImpl implements ExecutorProvider {
    private Lazy<ExecutorService> current = new Lazy<>(Executors::newSingleThreadExecutor);
    private boolean sat = false;

    @Override
    public ExecutorService getExecutor() {
        return current.get();
    }

    @Override
    public boolean hasExecutor() {
        return sat;
    }

    @Override
    public void setExecutor(ExecutorService executor) {
        current = new Lazy<>(executor);
        sat = true;
    }
}
