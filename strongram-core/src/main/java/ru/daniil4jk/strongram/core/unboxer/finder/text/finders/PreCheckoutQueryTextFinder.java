package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the invoice payload from a {@link PreCheckoutQuery}.
 */
public class PreCheckoutQueryTextFinder extends TextFinder<PreCheckoutQuery> {
    @Override
    public Class<PreCheckoutQuery> getInputClass() {
        return PreCheckoutQuery.class;
    }

    @Override
    public String parse(PreCheckoutQuery t) throws TelegramObjectFinderException {
        return t.getInvoicePayload();
    }
}
