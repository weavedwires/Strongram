package ru.daniil4jk.strongram.core.dialog.part;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.context.storage.Storage;

import java.util.function.BiConsumer;

/**
 * Manages notifications for a single dialog state.
 * Sends a "first" notification on first entry into the state and a "repeat" notification
 * on subsequent invocations, useful for re-prompting the user without repeating the same message.
 *
 * @param <ENUM> the enum type representing the dialog states
 */
@ToString
@EqualsAndHashCode
public class NotificationManager<ENUM extends Enum<ENUM>> {
    private final BiConsumer<RequestContext, Storage> firstNotification;
    private final BiConsumer<RequestContext, Storage> repeatNotification;
    private boolean firstNotificationSent;

    public NotificationManager(BiConsumer<RequestContext, Storage> firstNotification,
                               BiConsumer<RequestContext, Storage> repeatNotification) {
        this.firstNotification = firstNotification;
        this.repeatNotification = repeatNotification;
    }

    public void sendNotification(RequestContext rCtx, Storage dCtx) {
        if (firstNotificationSent) {
            repeatNotification.accept(rCtx, dCtx);
        } else {
            firstNotification.accept(rCtx, dCtx);
            firstNotificationSent = true;
        }
    }
}
