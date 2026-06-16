package ru.daniil4jk.strongram.core.context.request;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.daniil4jk.strongram.core.bot.Bot;
import ru.daniil4jk.strongram.core.context.storage.InMemoryStorage;
import ru.daniil4jk.strongram.core.context.storage.Storage;
import ru.daniil4jk.strongram.core.response.responder.factory.ResponderFactory;
import ru.daniil4jk.strongram.core.response.responder.factory.SmartResponderFactory;
import ru.daniil4jk.strongram.core.response.responder.factory.SmartResponderFactoryImpl;
import ru.daniil4jk.strongram.core.response.responder.smart.SmartResponder;
import ru.daniil4jk.strongram.core.unboxer.As;
import ru.daniil4jk.strongram.core.unboxer.Unboxer;
import ru.daniil4jk.strongram.core.unboxer.finder.uuid.TelegramUUIDFinderService;

@ToString
@EqualsAndHashCode
public class RequestContextImpl implements RequestContext {
    private final Storage storage = new InMemoryStorage();
    private final Bot bot;
    private final Update update;
    private final TelegramUUID uuid;
    private final SmartResponderFactory responderFactory;
    private final SmartResponder responder;

    public RequestContextImpl(Bot bot, Update update, ResponderFactory responderFactory) {
        this.bot = bot;
        this.update = update;
        this.uuid = TelegramUUIDFinderService.getInstance().findIn(update);
        this.responderFactory = new SmartResponderFactoryImpl(responderFactory);
        this.responder = this.responderFactory.createSmart(uuid, getRequest(As.callbackQuery()));
    }

    @Override
    public TelegramUUID getUUID() {
        return uuid;
    }

    @Override
    public Update getRequest() {
        return update;
    }

    @Override
    public <T> T getRequest(Unboxer<T> unboxer) {
        return unboxer.apply(update);
    }

    @Override
    public Storage getRequestScopeStorage() {
        return storage;
    }

    @Override
    public String getBotUsername() {
        return bot.getUsername();
    }

    @Override
    public SmartResponder getResponder() {
        return responder;
    }

    @Override
    public SmartResponderFactory getBotResponderFactory() {
        return responderFactory;
    }
}
