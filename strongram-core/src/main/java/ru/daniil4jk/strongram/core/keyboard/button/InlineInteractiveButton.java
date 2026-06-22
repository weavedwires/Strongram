package ru.daniil4jk.strongram.core.keyboard.button;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.telegram.telegrambots.meta.api.objects.LoginUrl;
import org.telegram.telegrambots.meta.api.objects.games.CallbackGame;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.CopyTextButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.SwitchInlineQueryChosenChat;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import ru.daniil4jk.strongram.core.context.request.RequestContext;

import java.util.function.Consumer;

/**
 * An {@link InlineKeyboardButton} that implements {@link InteractiveButton},
 * allowing an action callback to be attached to inline button presses.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class InlineInteractiveButton extends InlineKeyboardButton implements InteractiveButton {
    @JsonIgnore
    private final Consumer<RequestContext> action;

    public InlineInteractiveButton(@NonNull String text, Consumer<RequestContext> action) {
        super(text);
        this.action = action;
    }

    public InlineInteractiveButton(@NonNull String text, String callbackData, Consumer<RequestContext> action) {
        super(text);
        setCallbackData(callbackData);
        this.action = action;
    }

    public InlineInteractiveButton(@NonNull String text,
                                   String url,
                                   String callbackData,
                                   CallbackGame callbackGame,
                                   String switchInlineQuery,
                                   String switchInlineQueryCurrentChat,
                                   Boolean pay,
                                   LoginUrl loginUrl,
                                   WebAppInfo webApp,
                                   SwitchInlineQueryChosenChat switchInlineQueryChosenChat,
                                   CopyTextButton copyText, Consumer<RequestContext> action) {
        super(
                text, url, callbackData, callbackGame, switchInlineQuery, switchInlineQueryCurrentChat,
                pay, loginUrl, webApp, switchInlineQueryChosenChat, copyText
        );
        this.action = action;
    }

    @Override
    public void accept(RequestContext ctx) {
        action.accept(ctx);
    }
}
