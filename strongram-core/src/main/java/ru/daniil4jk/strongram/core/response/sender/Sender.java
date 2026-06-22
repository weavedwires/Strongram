package ru.daniil4jk.strongram.core.response.sender;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ru.daniil4jk.strongram.core.response.client.provider.TelegramClientProvider;
import ru.daniil4jk.strongram.core.response.dto.Response;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * Asynchronously dispatches single or batched {@link Response} objects
 * to the Telegram API using a provided executor and
 * {@link ru.daniil4jk.strongram.core.response.client.provider.TelegramClientProvider}.
 * Batched messages are sent with an optimal delay between them.
 */
@Slf4j
public class Sender {
    private final Supplier<ExecutorService> executor;
    private final TelegramClientProvider client;

    public Sender(Supplier<ExecutorService> executor, TelegramClientProvider client) {
        this.executor = executor;
        this.client = client;
    }

    public void sendUsingClient(@NotNull Response<?> message) {
        executor.get().execute(() -> sendSingleMessage(message, 1, 1));
    }

    public void sendAllUsingClient(@NotNull List<Response<?>> messages) {
        if (messages.isEmpty()) {
            return;
        }

        executor.get().execute(() -> processBatch(messages));
    }

    private void processBatch(@NotNull List<Response<?>> messages) {
        final long delay = Delays.getOptimalDelayInMills(messages);
        final int totalMessages = messages.size();

        log.debug("Starting to process batch of {} messages", totalMessages);

        for (int i = 0; i < totalMessages; i++) {
            Response<?> response = messages.get(i);
            int messageNumber = i + 1;

            sendSingleMessage(response, messageNumber, totalMessages);

            if (delay > 0) {
                try {
                    applyDelay(delay, messageNumber, totalMessages);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        log.debug("Finished sending batch of {} messages", totalMessages);
    }

    private void sendSingleMessage(
            @NotNull Response<?> response,
            int messageNumber,
            int totalMessages
    ) {
        try {
            response.sendUsing(client.getClient());
            log.trace(
                    "Message {}/{} sent successfully",
                    messageNumber,
                    totalMessages
            );
        } catch (Exception e) {
            log.warn(
                    "Batch processing interrupted at message {}/{}",
                    messageNumber,
                    totalMessages
            );
        }
    }

    private void applyDelay(
            long delayMillis,
            int messageNumber,
            int totalMessages
    ) throws InterruptedException {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            log.warn(
                    "Message sending interrupted after {}/{}",
                    messageNumber,
                    totalMessages
            );
            Thread.currentThread().interrupt();
            throw e;
        }
    }
}
