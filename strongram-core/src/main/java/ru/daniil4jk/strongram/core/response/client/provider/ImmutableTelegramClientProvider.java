package ru.daniil4jk.strongram.core.response.client.provider;

import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Supplier;

/**
 * An immutable {@link TelegramClientProvider} that accepts the client only
 * once and throws on any subsequent attempt to replace it.
 */
public class ImmutableTelegramClientProvider implements TelegramClientProvider {
    private final Supplier<TelegramClient> creator;
    private volatile TelegramClient client;

    public ImmutableTelegramClientProvider() {
        creator = null;
    }

    public ImmutableTelegramClientProvider(Supplier<TelegramClient> creator) {
        this.creator = creator;
    }

    @Override
    public TelegramClient getClient() {
        if (client == null && creator != null) {
            setClient(creator.get());
        }
        return client;
    }

    @Override
    public boolean hasClient() {
        return client != null;
    }

    @Override
    public void setClient(TelegramClient newClient) {
        if (newClient == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = newClient;
                } else {
                    throwClientCannotChangedException();
                }
            }
        } else {
            throwClientCannotChangedException();
        }
    }

    private void throwClientCannotChangedException() {
        throw new IllegalArgumentException("Client cannot be changed");
    }
}
