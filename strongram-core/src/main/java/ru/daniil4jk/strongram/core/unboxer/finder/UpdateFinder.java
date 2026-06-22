package ru.daniil4jk.strongram.core.unboxer.finder;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Base {@link Finder} that operates on an {@link Update}, routing to subclass
 * parsers based on the specific update sub-type present.
 *
 * @param <O> the output type produced by this finder
 */
public abstract class UpdateFinder<O> implements Finder<Update, O> {
    private static final String PROCESS_EDITED_ENV_NAME = "PROCESS_EDITED_MESSAGE_VARIANTS";
    private static final boolean PROCESS_EDITED_MESSAGE_VARIANTS =
                    System.getenv(PROCESS_EDITED_ENV_NAME) != null &&
                    !System.getenv(PROCESS_EDITED_ENV_NAME).isEmpty() &&
                    System.getenv(PROCESS_EDITED_ENV_NAME).equalsIgnoreCase("true");

    @Override
    public Class<Update> getInputClass() {
        return Update.class;
    }

    @Override
    public O parse(Update update) throws TelegramObjectFinderException {
        if (update.hasMessage()) return parseInternal(update.getMessage());
        if (update.hasInlineQuery()) return parseInternal(update.getInlineQuery());
        if (update.hasChosenInlineQuery()) return parseInternal(update.getChosenInlineQuery());
        if (update.hasCallbackQuery()) return parseInternal(update.getCallbackQuery());
        if (update.hasChannelPost()) return parseInternal(update.getChannelPost());
        if (update.hasShippingQuery()) return parseInternal(update.getShippingQuery());
        if (update.hasPreCheckoutQuery()) return parseInternal(update.getPreCheckoutQuery());
        if (update.hasPoll()) return parseInternal(update.getPoll());
        if (update.hasPollAnswer()) return parseInternal(update.getPollAnswer());
        if (update.hasMyChatMember()) return parseInternal(update.getMyChatMember());
        if (update.hasChatMember()) return parseInternal(update.getChatMember());
        if (update.hasChatJoinRequest()) return parseInternal(update.getChatJoinRequest());
        if (update.hasBusinessConnection()) return parseInternal(update.getBusinessConnection());
        if (update.hasBusinessMessage()) return parseInternal(update.getBusinessMessage());
        if (update.hasPaidMediaPurchased()) return parseInternal(update.getPaidMediaPurchased());

        if (PROCESS_EDITED_MESSAGE_VARIANTS) {
            if (update.hasEditedChannelPost()) return parseInternal(update.getEditedChannelPost());
            if (update.hasEditedMessage()) return parseInternal(update.getEditedMessage());
            if (update.hasEditedBusinessMessage()) return parseInternal(update.getEditedBuinessMessage());
            if (update.hasDeletedBusinessMessage()) return parseInternal(update.getDeletedBusinessMessages());
        }

        throw new TelegramObjectFinderException("empty update");
    }

    protected abstract <I extends BotApiObject> O parseInternal(I t);
}
