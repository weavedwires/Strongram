package ru.daniil4jk.strongram.core.dialog.part;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.daniil4jk.strongram.core.context.dialog.DialogContext;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.context.storage.Storage;
import ru.daniil4jk.strongram.core.filter.Filter;

/**
 * An abstract base for creating {@link DialogPart} implementations via subclassing.
 * Subclasses override {@link #getFilter()}, {@link #firstNotification},
 * {@link #repeatNotification}, and {@link #accept(RequestContext, DialogContext)}
 * to define dialog step behavior.
 *
 * @param <ENUM> the enum type representing the dialog states
 */
@ToString
@EqualsAndHashCode
public abstract class ExtendableDialogPart<ENUM extends Enum<ENUM>> implements DialogPart<ENUM> {
    private final NotificationManager<ENUM> notificationManager = new NotificationManager<>(
            this::firstNotification, this::repeatNotification
    );

    protected abstract Filter getFilter();

    protected abstract void firstNotification(RequestContext rCtx, Storage storage);

    protected abstract void repeatNotification(RequestContext rCtx, Storage storage);

    protected abstract void accept(RequestContext rCtx, DialogContext<ENUM> dCtx);

    @Getter(AccessLevel.PROTECTED)
    private DialogContext<ENUM> dCtx;

    @Override
    public boolean canAccept(RequestContext ctx) {
        return getFilter().test(ctx);
    }

    @Override
    public void injectDialogContext(DialogContext<ENUM> dCtx) {
        this.dCtx = dCtx;
    }

    @Override
    public void sendNotification(RequestContext ctx) {
        notificationManager.sendNotification(ctx, dCtx.getDialogScopeStorage());
    }

    @Override
    public void accept(RequestContext ctx) {
        accept(ctx, dCtx);
    }
}
