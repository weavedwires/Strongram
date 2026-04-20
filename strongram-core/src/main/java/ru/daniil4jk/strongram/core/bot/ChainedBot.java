package ru.daniil4jk.strongram.core.bot;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.daniil4jk.strongram.core.chain.Chain;
import ru.daniil4jk.strongram.core.chain.configurator.OrderConfigurator;
import ru.daniil4jk.strongram.core.chain.configurator.TunedOrderConfiguratorFactory;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.context.request.RequestContextImpl;
import ru.daniil4jk.strongram.core.downstream.CallbackWrapper;
import ru.daniil4jk.strongram.core.downstream.DownstreamHandler;
import ru.daniil4jk.strongram.core.response.responder.factory.ResponserFactoryImpl;
import ru.daniil4jk.strongram.core.response.responder.factory.SmartResponderFactoryImpl;
import ru.daniil4jk.strongram.core.response.sender.ResponseSink;
import ru.daniil4jk.strongram.core.upstream.UpstreamHandler;
import ru.daniil4jk.strongram.core.upstream.preinstalled.CannotProcessUpstreamHandler;
import ru.daniil4jk.strongram.core.util.Lazy;

import java.util.List;

@Slf4j
public abstract class ChainedBot extends BaseBot {
    private final Lazy<UpstreamHandler> upstreamChain = new Lazy<>(this::getUpstreamFirstHandler);
    private final Lazy<List<DownstreamHandler>> downstreamList = new Lazy<>(this::getDownstreamList);
    private final CallbackWrapper downstreamWrapper = new CallbackWrapper(downstreamList);

    public ChainedBot(@Nullable String username) {
        super(username);
    }

    @Override
    public void accept(Update update, ResponseSink tempCallback) {
        SmartResponderFactoryImpl resp = getResponserFactory();
        try {
            RequestContext ctx = new RequestContextImpl(this, update, resp);
            resp.setTempCallback(downstreamWrapper.wrap(ctx, tempCallback));
            upstreamChain.initOrGet().accept(ctx);
        } catch (Exception e) {
            log.error("Error occurred while chain processing update", e);
        } finally {
            resp.resetTempCallback();
        }
    }

    @Override
    public void setDefaultCallback(ResponseSink defaultCallback) {
        defaultCallback = downstreamWrapper.wrap(defaultCallback);
        super.setDefaultCallback(defaultCallback);
        ResponserFactoryImpl resp = getResponserFactory();
        resp.setPermanentCallback(defaultCallback);
    }

    private UpstreamHandler getUpstreamFirstHandler() {
        return getUpstreamChain().build();
    }
    protected Chain<UpstreamHandler> getUpstreamChain() {
        return new Chain<>(getUpstreamFactory());
    }
    private TunedOrderConfiguratorFactory<UpstreamHandler> getUpstreamFactory() {
        return new TunedOrderConfiguratorFactory<>(this::configureUpstream);
    }
    protected void configureUpstream(OrderConfigurator<UpstreamHandler> chain) {
        chain.add(new CannotProcessUpstreamHandler());
    }

    protected List<DownstreamHandler> getDownstreamList() {
        return getDownstreamFactory().get().asList();
    }
    private TunedOrderConfiguratorFactory<DownstreamHandler> getDownstreamFactory() {
        return new TunedOrderConfiguratorFactory<>(this::configureDownstream);
    }
    protected void configureDownstream(OrderConfigurator<DownstreamHandler> chain) {

    }
}
