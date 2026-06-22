package ru.daniil4jk.strongram.core.keyboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * Holds a {@link ReplyKeyboard} along with its {@link KeyboardType} (Reply or Inline)
 * to allow downstream processing to distinguish keyboard types at runtime.
 */
@Data
public class InteractiveKeyboardHolder {
    private final ReplyKeyboard keyboard;
    private final KeyboardType type;

    public InteractiveKeyboardHolder(ReplyKeyboardMarkup keyboard) {
        this.keyboard = keyboard;
        this.type = KeyboardType.Reply;
    }

    public InteractiveKeyboardHolder(InlineKeyboardMarkup keyboard) {
        this.keyboard = keyboard;
        this.type = KeyboardType.Inline;
    }

    @Getter
    @AllArgsConstructor
    public enum KeyboardType {
        Reply(ReplyKeyboardMarkup.class),
        Inline(InlineKeyboardMarkup.class);

        private final Class<? extends ReplyKeyboard> keyboardClass;
    }
}
