package ru.daniil4jk.strongram.core.response.responder;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.business.SetBusinessAccountProfilePhoto;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.stickers.*;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.daniil4jk.strongram.core.response.dto.ForgetResponse;
import ru.daniil4jk.strongram.core.response.dto.Response;
import ru.daniil4jk.strongram.core.response.dto.forsomething.ResponseForList;
import ru.daniil4jk.strongram.core.response.dto.forsomething.ResponseForObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * Skeletal implementation of {@link Responder} that delegates every
 * {@code send} and {@code sendForObject} overload to a single abstract
 * {@code sendInternal} method, wrapping each call in a {@link
 * ru.daniil4jk.strongram.core.response.dto.Response} wrapper.
 */
public abstract class AbstractResponder implements Responder {
    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> void send(Method response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SendDocument response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SendPhoto response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SetWebhook response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SendVideo response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SendVideoNote response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SendSticker response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SetBusinessAccountProfilePhoto response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SendAudio response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SendVoice response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SendMediaGroup response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SendPaidMedia response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SetChatPhoto response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(AddStickerToSet response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(ReplaceStickerInSet response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(SetStickerSetThumbnail response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(CreateNewStickerSet response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(UploadStickerFile response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public void send(EditMessageMedia response) {
        sendInternal(new ForgetResponse<>(response, c -> c.execute(response)));
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> sendForObject(Method response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendDocument response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendPhoto response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(SetWebhook response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendVideo response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendVideoNote response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendSticker response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(SetBusinessAccountProfilePhoto response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendAudio response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendVoice response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<ArrayList<Message>> sendForObject(SendMediaGroup response) {
        var wrapper = new ResponseForList<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<ArrayList<Message>> sendForObject(SendPaidMedia response) {
        var wrapper = new ResponseForList<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(SetChatPhoto response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(AddStickerToSet response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(ReplaceStickerInSet response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(SetStickerSetThumbnail response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(CreateNewStickerSet response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<org.telegram.telegrambots.meta.api.objects.File> sendForObject(UploadStickerFile response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    @Override
    public CompletableFuture<Serializable> sendForObject(EditMessageMedia response) {
        var wrapper = new ResponseForObject<>(response, c -> c.execute(response));
        sendInternal(wrapper);
        return wrapper.getObject();
    }

    protected abstract <T extends Response<?>> void sendInternal(T response);
}
