package ru.daniil4jk.strongram.core.dialog;

import ru.daniil4jk.strongram.core.context.request.TelegramUUID;

import java.util.List;

/**
 * Repository interface for managing Dialog entities associated with a Telegram user.
 * <p>
 * Implementations of this interface can use any {@code Database} or {@code InMemory} storage mechanisms.
 * </p>
 *
 * @see InMemoryDialogRepository
 * @see TelegramUUID
 * @see Dialog
 */
public interface DialogRepository {
    /**
     * Retrieves a list of dialogs associated with the given user UUID.
     *
     * @param uuid the TelegramUUID of the user
     * @return a list of dialogs, or an empty list if none exist
     */
    List<Dialog> get(TelegramUUID uuid);

    /**
     * Removes a specific dialog for the given user.
     *
     * @param uuid   the TelegramUUID of the user
     * @param dialog the dialog to remove
     */
    void remove(TelegramUUID uuid, Dialog dialog);

    /**
     * Adds a list of dialogs for the given user.
     * If dialogs already exist for the user, they may be replaced or merged
     * depending on the implementation.
     *
     * @param uuid    the TelegramUUID of the user
     * @param dialogs the list of dialogs to add
     */
    void addAll(TelegramUUID uuid, List<Dialog> dialogs);
}