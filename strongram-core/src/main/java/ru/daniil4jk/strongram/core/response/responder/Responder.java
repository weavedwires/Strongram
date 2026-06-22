package ru.daniil4jk.strongram.core.response.responder;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.business.SetBusinessAccountProfilePhoto;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.stickers.*;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * Defines the contract for sending Telegram API methods as fire-and-forget
 * or for retrieving a result. Provides typed overloads for every
 * supported sendable method type.
 */
public interface Responder {
    <T extends Serializable, Method extends BotApiMethod<T>> void send(Method response);

    void send(SendDocument response);

    void send(SendPhoto response);

    void send(SetWebhook response);

    void send(SendVideo response);

    void send(SendVideoNote response);

    void send(SendSticker response);

    void send(SetBusinessAccountProfilePhoto response);

    void send(SendAudio response);

    void send(SendVoice response);

    void send(SendMediaGroup response);

    void send(SendPaidMedia response);

    void send(SetChatPhoto response);

    void send(AddStickerToSet response);

    void send(ReplaceStickerInSet response);

    void send(SetStickerSetThumbnail response);

    void send(CreateNewStickerSet response);

    void send(UploadStickerFile response);

    void send(EditMessageMedia response);

    <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> sendForObject(Method response);

    CompletableFuture<Message> sendForObject(SendDocument response);

    CompletableFuture<Message> sendForObject(SendPhoto response);

    CompletableFuture<Boolean> sendForObject(SetWebhook response);

    CompletableFuture<Message> sendForObject(SendVideo response);

    CompletableFuture<Message> sendForObject(SendVideoNote response);

    CompletableFuture<Message> sendForObject(SendSticker response);

    CompletableFuture<Boolean> sendForObject(SetBusinessAccountProfilePhoto response);

    CompletableFuture<Message> sendForObject(SendAudio response);

    CompletableFuture<Message> sendForObject(SendVoice response);

    CompletableFuture<ArrayList<Message>> sendForObject(SendMediaGroup response);

    CompletableFuture<ArrayList<Message>> sendForObject(SendPaidMedia response);

    CompletableFuture<Boolean> sendForObject(SetChatPhoto response);

    CompletableFuture<Boolean> sendForObject(AddStickerToSet response);

    CompletableFuture<Boolean> sendForObject(ReplaceStickerInSet response);

    CompletableFuture<Boolean> sendForObject(SetStickerSetThumbnail response);

    CompletableFuture<Boolean> sendForObject(CreateNewStickerSet response);

    CompletableFuture<org.telegram.telegrambots.meta.api.objects.File> sendForObject(UploadStickerFile response);

    CompletableFuture<Serializable> sendForObject(EditMessageMedia response);
}