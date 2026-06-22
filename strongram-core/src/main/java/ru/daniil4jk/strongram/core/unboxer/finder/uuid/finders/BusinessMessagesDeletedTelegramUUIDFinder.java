package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.objects.business.BusinessMessagesDeleted;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the chat identity from {@link BusinessMessagesDeleted} as a {@link TelegramUUID}.
 */
public class BusinessMessagesDeletedTelegramUUIDFinder extends TelegramUUIDFinder<BusinessMessagesDeleted> {
    @Override
    public Class<BusinessMessagesDeleted> getInputClass() {
        return BusinessMessagesDeleted.class;
    }

    @Override
    public TelegramUUID parse(BusinessMessagesDeleted t) throws TelegramObjectFinderException {
        return new TelegramUUID(t.getChat(), null);
    }
}
