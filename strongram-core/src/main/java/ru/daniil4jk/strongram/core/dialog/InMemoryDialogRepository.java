package ru.daniil4jk.strongram.core.dialog;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of {@link DialogRepository}.
 * Stores dialogs in a {@link java.util.HashMap} keyed by {@link ru.daniil4jk.strongram.core.context.request.TelegramUUID}.
 */
@ToString
@EqualsAndHashCode
public class InMemoryDialogRepository implements DialogRepository {
    private final Map<TelegramUUID, List<Dialog>> storage = new HashMap<>();

    @Override
    public List<Dialog> get(TelegramUUID uuid) {
        return storage.get(uuid);
    }

    @Override
    public void remove(TelegramUUID uuid, Dialog dialog) {
        storage.compute(uuid,
                (u, dialogs) -> {
                    if (dialogs == null) {
                        throw new IllegalStateException("попытка удалить диалог у пользователя с несуществующим списком диалогов");
                    }
                    dialogs.remove(dialog);
                    if (dialogs.isEmpty()) {
                        return null;
                    }
                    return dialogs;
                }
        );
    }

    @Override
    public void addAll(TelegramUUID uuid, List<Dialog> newDialogs) {
        storage.computeIfAbsent(uuid, u -> new ArrayList<>())
                .addAll(newDialogs);
    }
}
