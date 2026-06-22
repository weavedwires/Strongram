package ru.daniil4jk.strongram.core.keyboard.button;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.*;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import ru.daniil4jk.strongram.core.context.request.RequestContext;

import java.util.function.Consumer;

/**
 * A {@link KeyboardButton} that implements {@link InteractiveButton},
 * allowing an action callback to be attached to reply keyboard button presses.
 * {@link #getCallbackData()} returns the button's text.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ReplyInteractiveButton extends KeyboardButton implements InteractiveButton {
    @JsonIgnore
    private final Consumer<RequestContext> action;

    public ReplyInteractiveButton(@NonNull String text, Consumer<RequestContext> action) {
        super(text);
        this.action = action;
    }

    public ReplyInteractiveButton(@NonNull String text,
                                  Boolean requestContact,
                                  Boolean requestLocation,
                                  KeyboardButtonPollType requestPoll,
                                  WebAppInfo webApp,
                                  KeyboardButtonRequestUser requestUser,
                                  KeyboardButtonRequestChat requestChat,
                                  KeyboardButtonRequestUsers requestUsers,
                                  Consumer<RequestContext> action) {
        super(text, requestContact, requestLocation, requestPoll, webApp, requestUser, requestChat, requestUsers);
        this.action = action;
    }

    @Override
    public void accept(RequestContext ctx) {
        action.accept(ctx);
    }

    @Override
    public String getCallbackData() {
        return getText();
    }
}
