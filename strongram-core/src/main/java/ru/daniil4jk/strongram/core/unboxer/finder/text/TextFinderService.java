package ru.daniil4jk.strongram.core.unboxer.finder.text;

import lombok.Getter;
import ru.daniil4jk.strongram.core.unboxer.finder.FinderService;

/**
 * Singleton {@link FinderService} that finds and extracts {@link String} text from Telegram objects.
 */
public class TextFinderService extends FinderService<String> {
    @Getter
    private static final TextFinderService instance = new TextFinderService();

    private TextFinderService() {
    }

    @Override
    protected Class<String> getOutputClass() {
        return String.class;
    }
}
