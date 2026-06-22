package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.objects.business.BusinessMessagesDeleted;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * {@link TextFinder} for {@link BusinessMessagesDeleted} — always throws because this type has no text payload.
 */
public class BusinessMessagesDeletedTextFinder extends TextFinder<BusinessMessagesDeleted> {
    @Override
    public Class<BusinessMessagesDeleted> getInputClass() {
        return BusinessMessagesDeleted.class;
    }

    @Override
    public String parse(BusinessMessagesDeleted t) throws TelegramObjectFinderException {
        throwNotContainsStringException(t.getClass());
        return null;
    }
}
