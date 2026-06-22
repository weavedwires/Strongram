package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the callback data from a {@link CallbackQuery}.
 */
public class CallbackQueryTextFinder extends TextFinder<CallbackQuery> {
    @Override
    public Class<CallbackQuery> getInputClass() {
        return CallbackQuery.class;
    }

    @Override
    public String parse(CallbackQuery t) throws TelegramObjectFinderException {
        return t.getData();
    }
}
