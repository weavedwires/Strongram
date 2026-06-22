package ru.daniil4jk.strongram.core.upstream;

import ru.daniil4jk.strongram.core.chain.NextConsumer;
import ru.daniil4jk.strongram.core.context.request.RequestContext;

import java.util.function.Consumer;

/**
 * Contract for handlers in the upstream (inbound) processing chain.
 * Each handler receives a {@link RequestContext} and can delegate to the next handler
 * via the {@link NextConsumer} mechanism.
 */
public interface UpstreamHandler extends Consumer<RequestContext>, NextConsumer<UpstreamHandler> {
}
