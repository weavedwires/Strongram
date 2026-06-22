package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import ru.daniil4jk.strongram.core.unboxer.finder.UpdateFinder;
import ru.daniil4jk.strongram.core.unboxer.finder.text.TextFinderService;

/**
 * Extracts text from an {@link Update} by delegating to {@link ru.daniil4jk.strongram.core.unboxer.finder.text.TextFinderService}.
 */
public class UpdateTextFinder extends UpdateFinder<String> {
    @Override
    public Class<String> getOutputClass() {
        return String.class;
    }

    @Override
    protected <I extends BotApiObject> String parseInternal(I t) {
        return TextFinderService.getInstance().findIn(t);
    }
}
