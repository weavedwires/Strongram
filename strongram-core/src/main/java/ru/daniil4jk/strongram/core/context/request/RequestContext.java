package ru.daniil4jk.strongram.core.context.request;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.daniil4jk.strongram.core.context.storage.Storage;
import ru.daniil4jk.strongram.core.response.responder.factory.SmartResponderFactory;
import ru.daniil4jk.strongram.core.response.responder.smart.SmartResponder;
import ru.daniil4jk.strongram.core.unboxer.Unboxer;

/**
 * Per-request context providing access to the Telegram {@link Update}, the
 * bot's username, request-scoped {@link Storage}, and a {@link SmartResponder}
 * for sending responses.
 */
public interface RequestContext {
    TelegramUUID getUUID();

    Update getRequest();

    <T> T getRequest(Unboxer<T> unboxer);

    Storage getRequestScopeStorage();

    String getBotUsername();

    SmartResponder getResponder();

    SmartResponderFactory getBotResponderFactory();
}