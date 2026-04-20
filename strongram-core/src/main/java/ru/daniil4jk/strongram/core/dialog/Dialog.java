package ru.daniil4jk.strongram.core.dialog;

import org.jetbrains.annotations.NotNull;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.dialog.part.DialogPart;
import ru.daniil4jk.strongram.core.upstream.preinstalled.DialogUpstreamHandler;

import java.util.function.Consumer;

/**
 * Represents a conversational dialog.
 * <p>
 * A {@code Dialog} is responsible for handling a sequence of user interactions,
 * maintaining dialog state, and determining when it can process incoming requests.
 * </p>
 *
 * <p>Implementations of this interface are expected to manage their own internal state
 * and decide whether they can handle a given request based on context such as user,
 * chat, or input data.</p>
 */
public interface Dialog extends Consumer<RequestContext> {
    /**
     * Sends a notification or initial message to the user as part of the dialog flow.
     * <p>
     * This method is typically called when the dialog is started or when a reminder
     * or update needs to be sent during the conversation.
     * </p>
     *
     * @param ctx the request context containing information about the current request,
     *            such as the user, chat, and bot instance; must not be null
     * @throws NullPointerException if {@code ctx} is null
     */
    void sendNotification(RequestContext ctx);

    /**
     * Checks whether the dialog has been stopped and should no longer process requests.
     * <p>
     * A stopped dialog usually indicates that the conversation has ended,
     * either successfully or due to cancellation or timeout.
     * </p>
     *
     * @return {@code true} if the dialog is stopped and cannot accept further input;
     * {@code false} if the dialog is active and can continue processing
     */
    boolean isStopped();

    /**
     * Determines whether this dialog can accept and process the given request context.
     * <p>
     * This method is used by the {@link DialogUpstreamHandler} to route incoming updates to the appropriate dialog.
     * Delegates to current DialogPart`s {@link DialogPart#canAccept(RequestContext)}
     * </p>
     *
     * @param ctx the request context to evaluate; must not be null
     * @return {@code true} if this dialog can handle the request, {@code false} if can`t
     * @throws NullPointerException if {@code ctx} is null
     */
    boolean canAccept(@NotNull RequestContext ctx);

    /**
     * Processes the incoming request context as part of the dialog's conversation flow.
     * <p>
     * This method is called when a request is routed to this dialog
     * after {@link #canAccept(RequestContext)} returns {@code true}.
     * It delegates to current DialogPart`s {@link DialogPart#accept(RequestContext)}
     * for handling user input, updating state, and sending responses.
     * </p>
     *
     * @param ctx the request context to process; must not be null
     * @throws NullPointerException if {@code ctx} is null
     */
    @Override
    void accept(@NotNull RequestContext ctx);

    /**
     * Returns the object used as a lock for synchronizing access to this dialog.
     * <p>
     * This lock is used by the {@code DialogHandler} to ensure thread-safe execution
     * when multiple threads want to process updates for the same dialog instance.
     * </p>
     *
     * @return the lock object associated with this dialog
     */
    @NotNull Object getLock();
}