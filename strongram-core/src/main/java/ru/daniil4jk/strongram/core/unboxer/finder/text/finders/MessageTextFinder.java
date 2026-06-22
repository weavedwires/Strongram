package ru.daniil4jk.strongram.core.unboxer.finder.text.finders;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.daniil4jk.strongram.core.unboxer.finder.TelegramObjectFinderException;

/**
 * Extracts the text or caption from a {@link Message}.
 */
public class MessageTextFinder extends TextFinder<Message> {
    @Override
    public Class<Message> getInputClass() {
        return Message.class;
    }

    @Override
    public String parse(Message t) throws TelegramObjectFinderException {
        if (t.hasText()) return t.getText();
        if (t.hasCaption()) return t.getCaption();
        throw new TelegramObjectFinderException("%s %s not contains text or caption"
                .formatted(t.getClass().getSimpleName(), t));
    }
}
