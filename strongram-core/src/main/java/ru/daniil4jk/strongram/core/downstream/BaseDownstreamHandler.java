package ru.daniil4jk.strongram.core.downstream;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import ru.daniil4jk.strongram.core.context.request.RequestContext;

import java.util.Optional;

/**
 * Abstract base for downstream handlers. Subclasses implement
 * {@link #process(Optional, PartialBotApiMethod)} to intercept or modify outgoing
 * messages before they are sent to Telegram.
 */
public abstract class BaseDownstreamHandler implements DownstreamHandler {
    @Override
    public void accept(Optional<RequestContext> ctx, PartialBotApiMethod<?> message) {
        process(ctx, message);
    }

    protected abstract void process(Optional<RequestContext> ctx, PartialBotApiMethod<?> message);
}
