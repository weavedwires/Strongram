package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.objects.payments.PaidMediaPurchased;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the paid media payload from a {@link PaidMediaPurchased}.
 */
public class PaidMediaPurchasedTextFinder extends TextFinder<PaidMediaPurchased> {
    @Override
    public Class<PaidMediaPurchased> getInputClass() {
        return PaidMediaPurchased.class;
    }

    @Override
    public String parse(PaidMediaPurchased t) throws TelegramObjectFinderException {
        return t.getPaidMediaPayload();
    }
}
