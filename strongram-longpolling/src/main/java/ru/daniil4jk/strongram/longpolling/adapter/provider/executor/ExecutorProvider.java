package ru.daniil4jk.strongram.longpolling.adapter.provider.executor;

import java.util.concurrent.ExecutorService;

public interface ExecutorProvider {
    ExecutorService getExecutor();

    boolean hasExecutor();

    void setExecutor(ExecutorService client);
}
