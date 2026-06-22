package ru.daniil4jk.strongram.core.response.responder.factory;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.response.responder.sink.SinkResponder;
import ru.daniil4jk.strongram.core.response.responder.smart.SmartResponder;
import ru.daniil4jk.strongram.core.response.responder.smart.SmartResponderImpl;

/**
 * Default implementation of {@link SmartResponderFactory}. Wraps a
 * delegate {@link ResponderFactory} and produces {@link
 * ru.daniil4jk.strongram.core.response.responder.smart.SmartResponderImpl}
 * instances bound to a chat UUID and optional callback query.
 */
@RequiredArgsConstructor
public class SmartResponderFactoryImpl implements ResponderFactory, SmartResponderFactory {
    private final ResponderFactory inherit;

    @Override
    public SmartResponder createSmart(TelegramUUID uuid, CallbackQuery queryToAnswer) {
        return new SmartResponderImpl(uuid, create(), queryToAnswer);
    }

    @Override
    public SinkResponder create() {
        return inherit.create();
    }
}
