package ru.daniil4jk.strongram.longpolling.adapter.provider.executor;

import java.util.concurrent.ExecutorService;

/**
 * Provides a mutable {@link java.util.concurrent.ExecutorService}. Allows
 * components to accept and manage an executor for processing updates or sending responses.
 */
public interface ExecutorProvider {
    ExecutorService getExecutor();

    boolean hasExecutor();

    void setExecutor(ExecutorService client);
}
