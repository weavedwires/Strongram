package ru.daniil4jk.strongram.core.dialog.part;

import ru.daniil4jk.strongram.core.context.dialog.DialogContext;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.dialog.Dialog;
import ru.daniil4jk.strongram.core.filter.Filter;

import java.util.function.Consumer;

/**
 * Represents a single step (state) in a conversational dialog flow.
 * Each implementation corresponds to a specific state in the dialog, defined by an enum type.
 *
 * @param <ENUM> - The enum type representing the states of the dialog (e.g. {@code DialogState.SET_DELIVERY_ADDRESS}).
 */
public interface DialogPart<ENUM extends Enum<ENUM>> extends Consumer<RequestContext> {

    /**
     * Called when entering this dialog state to sendUsing a prompt or message to the user.
     * For example: asking for input like "Please enter your delivery address".
     *
     * @param ctx The current request context containing info about the incoming update.
     */
    void sendNotification(RequestContext ctx);

    /**
     * Determines whether this part can accept and process the given request context.
     * <p>
     * This method is called by the {@link Dialog} to route incoming updates to the appropriate dialog.
     * The decision may be based on user ID, chat type, current dialog state, or other criteria.
     * You can use {@link Filter} to implement more complex logic.
     * </p>
     *
     * @param ctx the request context to evaluate; must not be null
     * @return {@code true} if this dialog can handle the request, {@code false} if can`t
     * @throws NullPointerException if {@code ctx} is null
     */
    boolean canAccept(RequestContext ctx);

    /**
     * Processes the request when this part is active and {@link #canAccept} returns true.
     * This is where the main logic of the dialog step is implemented.
     *
     * @param context The current request context.
     */
    @Override
    void accept(RequestContext context);

    /**
     * Injects the dialog context, providing access to shared user data,
     * state transitions, and other dialog-specific tools.
     *
     * @param dCtx The dialog context associated with the current conversation.
     * @apiNote This method requires to be called after adding part to the dialog.
     */
    void injectDialogContext(DialogContext<ENUM> dCtx);
}