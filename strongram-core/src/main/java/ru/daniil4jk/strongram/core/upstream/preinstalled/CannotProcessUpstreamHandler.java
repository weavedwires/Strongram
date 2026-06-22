package ru.daniil4jk.strongram.core.upstream.preinstalled;

import lombok.RequiredArgsConstructor;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.unboxer.As;
import ru.daniil4jk.strongram.core.upstream.BaseUpstreamHandler;

/**
 * Terminal fallback handler that replies with a default "cannot process" message
 * when no other handler in the chain was able to process the update.
 */
@RequiredArgsConstructor
public class CannotProcessUpstreamHandler extends BaseUpstreamHandler {
    private final String text;

    public CannotProcessUpstreamHandler() {
        this.text = "I cat`t explain this request\uD83D\uDE14";
    }

    @Override
    protected final void process(RequestContext ctx) {
        try {
            if (ctx.getUUID().chat().isUserChat()) {
                ctx.getResponder().send(text);
                return;
            }
        } catch (Exception e) { /* сообщение предназначено не этому боту, игнорим */ }
        try {
            if (ctx.getRequest(As.messageText()) != null) {
                ctx.getResponder().send(text);
            }
        } catch (Exception e) { /* на этот тип сообщений не нужно отвечать, игнорим */ }
    }
}
