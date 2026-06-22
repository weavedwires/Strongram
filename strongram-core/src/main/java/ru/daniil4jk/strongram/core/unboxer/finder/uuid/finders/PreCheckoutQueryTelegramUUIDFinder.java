package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the user identity from a {@link PreCheckoutQuery} as a {@link TelegramUUID}.
 */
public class PreCheckoutQueryTelegramUUIDFinder extends TelegramUUIDFinder<PreCheckoutQuery> {
    @Override
    public Class<PreCheckoutQuery> getInputClass() {
        return PreCheckoutQuery.class;
    }

    @Override
    public TelegramUUID parse(PreCheckoutQuery t) throws TelegramObjectFinderException {
        return new TelegramUUID(null, t.getFrom());
    }
}
