package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.objects.business.BusinessConnection;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * {@link TextFinder} for {@link BusinessConnection} — always throws because this type has no text payload.
 */
public class BusinessConnectionTextFinder extends TextFinder<BusinessConnection> {
    @Override
    public Class<BusinessConnection> getInputClass() {
        return BusinessConnection.class;
    }

    @Override
    public String parse(BusinessConnection t) throws TelegramObjectFinderException {
        throwNotContainsStringException(t.getClass());
        return null;
    }
}
