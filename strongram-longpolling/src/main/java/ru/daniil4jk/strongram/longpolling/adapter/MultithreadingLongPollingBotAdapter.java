package ru.daniil4jk.strongram.longpolling.adapter;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.daniil4jk.strongram.core.bot.Bot;
import ru.daniil4jk.strongram.core.util.DefaultExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A variant of {@link LongPollingBotAdapter} that processes each update on a separate
 * thread from a configurable {@link java.util.concurrent.ExecutorService}.
 */
public class MultithreadingLongPollingBotAdapter extends LongPollingBotAdapter {
    private final ExecutorService executor;

    public MultithreadingLongPollingBotAdapter(String token, Bot bot) {
        this(
                token,
                bot,
                DefaultExecutor.initOrGet(MultithreadingLongPollingBotAdapter.class.getName())
        );
    }

    public MultithreadingLongPollingBotAdapter(String token, Bot bot, ScheduledExecutorService executor) {
        this(
                token,
                bot,
                executor,
                executor
        );
    }

    public MultithreadingLongPollingBotAdapter(String token,
                                               Bot bot,
                                               ExecutorService mainExecutor,
                                               ScheduledExecutorService sendExecutor) {
        super(token, bot, sendExecutor);
        this.executor = mainExecutor;
    }

    @Override
    public void consumeSingle(Update update) {
        executor.execute(() -> super.consumeSingle(update));
    }
}
