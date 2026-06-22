package ru.daniil4jk.strongram.core.unboxer.finder.uuid.finders;

import org.telegram.telegrambots.meta.api.objects.payments.ShippingQuery;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the user identity from a {@link ShippingQuery} as a {@link TelegramUUID}.
 */
public class ShippingQueryTelegramUUIDFinder extends TelegramUUIDFinder<ShippingQuery> {
    @Override
    public Class<ShippingQuery> getInputClass() {
        return ShippingQuery.class;
    }

    @Override
    public TelegramUUID parse(ShippingQuery t) throws TelegramObjectFinderException {
        return new TelegramUUID(null, t.getFrom());
    }
}
