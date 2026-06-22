package ru.daniil4jk.strongram.core.upstream.preinstalled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.report.exception.ExceptionFormatter;
import ru.daniil4jk.strongram.core.report.exception.ExceptionFormatters;
import ru.daniil4jk.strongram.core.upstream.BaseUpstreamHandler;

/**
 * Try/catch wrapper around the upstream chain. Catches exceptions thrown by downstream
 * handlers, logs them, and sends a formatted error message to the user via an
 * {@link ExceptionFormatter}.
 */
@Slf4j
@RequiredArgsConstructor
public class ExceptionReportUpstreamHandler extends BaseUpstreamHandler {
    private final ExceptionFormatter formatter;

    public ExceptionReportUpstreamHandler() {
        formatter = ExceptionFormatters.info();
    }

    @Override
    protected final void process(RequestContext ctx) {
        try {
            processNext(ctx);
        } catch (Exception e) {
            log.error("Произошла ошибка внутри цепочки", e);
            try {
                ctx.getResponder().send(formatter.apply(e));
            } catch (Exception e2) { /* не можем отправить сообщение об ошибке, всё очень плохо */ }
        }
    }
}