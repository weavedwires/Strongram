package ru.daniil4jk.strongram.core.dialog;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.daniil4jk.strongram.core.context.dialog.DialogContext;
import ru.daniil4jk.strongram.core.context.dialog.DialogContextImpl;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.context.storage.Storage;
import ru.daniil4jk.strongram.core.dialog.part.DialogPart;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A state-machine-based implementation of {@link Dialog}.
 * Uses an enum to define dialog states and maps each state to a {@link DialogPart}
 * that handles requests while the dialog is in that state.
 *
 * @param <ENUM> the enum type representing the dialog states
 */
@ToString
@EqualsAndHashCode
public class DialogImpl<ENUM extends Enum<ENUM>> implements Dialog {
    private final EnumMap<ENUM, DialogPart<ENUM>> parts;
    private final DialogContext<ENUM> dialogContext;

    private DialogImpl(@NotNull Class<ENUM> enumClass,
                       @NotNull DialogContext<ENUM> ctx,
                       @NotNull Map<ENUM, DialogPart<ENUM>> parts) {
        this.dialogContext = ctx;

        parts.values().forEach(p -> p.injectDialogContext(dialogContext));
        this.parts = new EnumMap<>(parts);
    }

    public DialogImpl(@NotNull ENUM initState) {
        this.parts = new EnumMap<>(initState.getDeclaringClass());
        this.dialogContext = new DialogContextImpl<>(initState);
    }

    public void add(ENUM state, DialogPart<ENUM> part) {
        check();
        part.injectDialogContext(dialogContext);
        parts.put(state, part);
    }

    @Override
    public void sendNotification(RequestContext ctx) {
        check();
        getCurrentPart().sendNotification(ctx);
    }

    @Override
    public synchronized void accept(@NotNull RequestContext ctx) {
        check();
        getCurrentPart().accept(ctx);
    }

    @Override
    public boolean isStopped() {
        return dialogContext.isStopped();
    }

    private void check() {
        if (isStopped()) {
            throw new IllegalStateException("dialog is stopped!");
        }
    }

    @Override
    public boolean canAccept(@NotNull RequestContext ctx) {
        return getCurrentPart().canAccept(ctx);
    }

    private DialogPart<ENUM> getCurrentPart() {
        return parts.get(dialogContext.getState());
    }

    private final Object lock = new Object();

    @Override
    public @NotNull Object getLock() {
        check();
        return lock;
    }

    @Contract(" -> new")
    public static <ENUM extends Enum<ENUM>> @NotNull Builder<ENUM> builder() {
        return new Builder<>();
    }

    @ToString
    public static class Builder<ENUM extends Enum<ENUM>> {
        private ENUM initState;
        private Map<ENUM, DialogPart<ENUM>> parts = new HashMap<>();
        private Storage storage;

        public Builder<ENUM> initState(@NotNull ENUM initState) {
            this.initState = initState;
            return this;
        }

        public Builder<ENUM> part(ENUM state, DialogPart<ENUM> part) {
            this.parts.put(state, part);
            return this;
        }

        public Builder<ENUM> parts(Map<ENUM, DialogPart<ENUM>> parts) {
            this.parts = parts;
            return this;
        }

        public Builder<ENUM> storage(Storage storage) {
            this.storage = storage;
            return this;
        }

        public DialogImpl<ENUM> build() {
            Objects.requireNonNull(
                    initState,
                    "initState cannot be null, because dialog cannot start without it"
            );

            DialogContext<ENUM> ctx = new DialogContextImpl<>(initState, storage);

            return new DialogImpl<>(initState.getDeclaringClass(), ctx, parts);
        }
    }
}