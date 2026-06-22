package ru.daniil4jk.strongram.core.context.dialog;

import ru.daniil4jk.strongram.core.context.storage.Storage;

/**
 * Contract for a dialog session scoped to a single conversation. Manages
 * an enum-based state machine, dialog-scoped {@link Storage}, and a
 * stopped flag to signal dialog completion.
 *
 * @param <ENUM> the enum type representing dialog states
 */
public interface DialogContext<ENUM extends Enum<ENUM>> {
    Storage getDialogScopeStorage();

    void setState(ENUM toState);

    ENUM getState();

    void stop();

    boolean isStopped();
}
