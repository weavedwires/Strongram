package ru.daniil4jk.strongram.core.downstream;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import ru.daniil4jk.strongram.core.context.request.RequestContext;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Contract for handlers in the downstream (outbound) processing chain.
 * Each handler receives the original {@link RequestContext} (if available) and the outgoing
 * {@link PartialBotApiMethod} before it is sent to Telegram.
 */
public interface DownstreamHandler extends BiConsumer<Optional<RequestContext>, PartialBotApiMethod<?>> {
}
