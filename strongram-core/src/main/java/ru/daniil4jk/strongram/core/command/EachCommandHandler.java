package ru.daniil4jk.strongram.core.command;

import ru.daniil4jk.strongram.core.context.request.RequestContext;

import java.util.function.BiConsumer;

/**
 * Contract for handling a single bot command. Accepts a {@link RequestContext}
 * and an array of arguments parsed from the command text.
 */
public interface EachCommandHandler extends BiConsumer<RequestContext, String[]> {
}
