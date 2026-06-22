package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.objects.business.BusinessConnection;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the user and chat identity from a {@link BusinessConnection} as a {@link TelegramUUID}.
 */
public class BusinessConnectionTelegramUUIDFinder extends TelegramUUIDFinder<BusinessConnection> {
    @Override
    public Class<BusinessConnection> getInputClass() {
        return BusinessConnection.class;
    }

    @Override
    public TelegramUUID parse(BusinessConnection t) throws TelegramObjectFinderException {
        return new TelegramUUID(new Chat(t.getUserChatId(), "private"), t.getUser());
    }
}
