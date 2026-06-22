package ru.daniil4jk.strongram.core.bot;

import ru.daniil4jk.strongram.core.response.responder.factory.SinkResponderFactory;

/**
 * Contract for a Telegram bot. Defines the bot's username and the
 * {@link SinkResponderFactory} used to create responders for outgoing messages.
 */
public interface Bot extends UpdateProcessor {
    String getUsername();

    SinkResponderFactory getResponderFactory();
}
