package ru.daniil4jk.strongram.core.upstream;

import lombok.Setter;
import ru.daniil4jk.strongram.core.context.request.RequestContext;

/**
 * Abstract base for upstream handlers. Subclasses implement {@link #process(RequestContext)}
 * and call {@link #processNext(RequestContext)} to pass control to the next handler in the chain.
 */
public abstract class BaseUpstreamHandler implements UpstreamHandler {
    @Setter
    private UpstreamHandler next;

    @Override
    public void accept(RequestContext ctx) {
        process(ctx);
    }

    protected abstract void process(RequestContext ctx);

    protected final void processNext(RequestContext ctx) {
        if (next != null) {
            next.accept(ctx);
        }
    }
}
