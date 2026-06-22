package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.objects.payments.ShippingQuery;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the invoice payload from a {@link ShippingQuery}.
 */
public class ShippingQueryTextFinder extends TextFinder<ShippingQuery> {
    @Override
    public Class<ShippingQuery> getInputClass() {
        return ShippingQuery.class;
    }

    @Override
    public String parse(ShippingQuery t) throws TelegramObjectFinderException {
        return t.getInvoicePayload();
    }
}
