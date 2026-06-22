package ru.daniil4jk.strongram.core.response.responder.smart;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.business.SetBusinessAccountProfilePhoto;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.stickers.*;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.response.responder.sink.SinkResponder;
import ru.daniil4jk.strongram.core.util.message.LongMessage;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link SmartResponder}. Delegates low-level
 * API calls to a {@link ru.daniil4jk.strongram.core.response.responder.sink.SinkResponder}
 * and handles text splitting, media attachment, and callback answer logic.
 */
@ToString
@EqualsAndHashCode
public class SmartResponderImpl implements SmartResponder {
    private final Long id;
    private final SinkResponder inherit;
    private final CallbackQuery queryToAnswer;

    public SmartResponderImpl(Long id, SinkResponder inherit) {
        this(id, inherit, null);
    }

    public SmartResponderImpl(TelegramUUID uuid, SinkResponder inherit) {
        this(uuid, inherit, null);
    }

    public SmartResponderImpl(TelegramUUID uuid, SinkResponder inherit, @Nullable CallbackQuery queryToAnswer) {
        this(uuid.getReplyChatId(), inherit, queryToAnswer);
    }

    public SmartResponderImpl(Long id, SinkResponder inherit, @Nullable CallbackQuery queryToAnswer) {
        this.id = id;
        this.inherit = inherit;
        this.queryToAnswer = queryToAnswer;
    }

    @Override
    public void send(String text) {
        send(id, text);
    }

    @Override
    public void send(String text, ReplyKeyboard keyboard) {
        send(id, text, keyboard);
    }

    @Override
    public CompletableFuture<List<Message>> sendForObject(String text) {
        return sendForObject(id, text);
    }

    @Override
    public CompletableFuture<List<Message>> sendForObject(String text, ReplyKeyboard keyboard) {
        return sendForObject(id, text, keyboard);
    }

    @Override
    public void send(String text, File file, MediaType type) {
        send(id, text, file, type);
    }

    @Override
    public void send(String text, File file, MediaType type, ReplyKeyboard keyboard) {
        send(id, text, file, type, keyboard);
    }

    @Override
    public CompletableFuture<List<Message>> sendForObject(String text, File file, MediaType type) {
        return sendForObject(id, text, file, type);
    }

    @Override
    public CompletableFuture<List<Message>> sendForObject(String text, File file, MediaType type, ReplyKeyboard keyboard) {
        return sendForObject(id, text, file, type, keyboard);
    }

    @Override
    public void send(Long id, String text) {
        send(id, text, null);
    }

    @Override
    public void send(Long id, String text, File file, MediaType type) {
        send(id, text, file, type, null);
    }

    @Override
    public void send(Long id, String text, ReplyKeyboard keyboard) {
        List<String> messageTexts = new LongMessage(text).asLegalLengthMessageList();

        for (String part : messageTexts) {
            sendSingle(id, part, keyboard);
        }
    }

    @Override
    public void send(Long id, String text, File file, MediaType type, ReplyKeyboard keyboard) {
        List<String> messageTexts = new LongMessage(text).asLegalLengthMessageList();

        sendSingle(id, messageTexts.remove(0), file, type, keyboard);
        for (String part : messageTexts) {
            sendSingle(id, part, keyboard);
        }
    }

    @Override
    public CompletableFuture<List<Message>> sendForObject(Long id, String text) {
        return sendForObject(id, text, null);
    }

    @Override
    public CompletableFuture<List<Message>> sendForObject(Long id, String text, File file, MediaType type) {
        return sendForObject(id, text, file, type, null);
    }

    @Override
    public CompletableFuture<List<Message>> sendForObject(Long id, String text, ReplyKeyboard keyboard) {
        List<String> messageTexts = new LongMessage(text).asLegalLengthMessageList();

        return CompletableFuture.supplyAsync(() ->
                messageTexts.stream()
                        .map(s -> sendForObjectSingle(id, s, keyboard))
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public CompletableFuture<List<Message>> sendForObject(Long id, String text, File file, MediaType type, ReplyKeyboard keyboard) {
        List<String> messageTexts = new LongMessage(text).asLegalLengthMessageList();
        List<CompletableFuture<Message>> responses = new ArrayList<>(messageTexts.size());

        var first = sendForObjectSingle(id, messageTexts.remove(0), file, type, keyboard);
        responses.add(first);

        messageTexts.stream()
                .map(s -> sendForObjectSingle(id, s, keyboard))
                .forEach(responses::add);

        return CompletableFuture.supplyAsync(() ->
                responses.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );
    }

    private void sendSingle(Long id, String text, ReplyKeyboard keyboard) {
        send(
                SendMessage.builder()
                        .text(text)
                        .replyMarkup(keyboard)
                        .chatId(id)
                        .build()
        );
    }

    private CompletableFuture<Message> sendForObjectSingle(Long id, String text, ReplyKeyboard keyboard) {
        return sendForObject(
                SendMessage.builder()
                        .text(text)
                        .replyMarkup(keyboard)
                        .chatId(id)
                        .build()
        );
    }

    private void sendSingle(Long id, String text, File file, MediaType type, ReplyKeyboard keyboard) {
        SendMediaBotMethod<Message> method = createMethod(id, text, file, type, keyboard);
        switch (type) {
            case Photo -> inherit.send((SendPhoto) method);
            case Video -> inherit.send((SendVideo) method);
            case Audio -> inherit.send((SendAudio) method);
            case Document -> inherit.send((SendDocument) method);
            case Voice -> inherit.send((SendVoice) method);
        }
    }

    private CompletableFuture<Message> sendForObjectSingle(Long id, String text, File file, MediaType type, ReplyKeyboard keyboard) {
        SendMediaBotMethod<Message> method = createMethod(id, text, file, type, keyboard);
        return switch (type) {
            case Photo -> inherit.sendForObject((SendPhoto) method);
            case Video -> inherit.sendForObject((SendVideo) method);
            case Audio -> inherit.sendForObject((SendAudio) method);
            case Document -> inherit.sendForObject((SendDocument) method);
            case Voice -> inherit.sendForObject((SendVoice) method);
        };
    }

    private SendMediaBotMethod<Message> createMethod(Long id, String text, File file, MediaType type, ReplyKeyboard keyboard) {
        return switch (type) {
            case Photo -> SendPhoto.builder()
                    .photo(new InputFile(file))
                    .caption(text)
                    .replyMarkup(keyboard)
                    .chatId(id)
                    .build();
            case Video -> SendVideo.builder()
                    .video(new InputFile(file))
                    .caption(text)
                    .replyMarkup(keyboard)
                    .chatId(id)
                    .build();
            case Audio -> SendAudio.builder()
                    .audio(new InputFile(file))
                    .caption(text)
                    .replyMarkup(keyboard)
                    .chatId(id)
                    .build();
            case Voice -> SendVoice.builder()
                    .voice(new InputFile(file))
                    .caption(text)
                    .replyMarkup(keyboard)
                    .chatId(id)
                    .build();
            case Document -> SendDocument.builder()
                    .document(new InputFile(file))
                    .caption(text)
                    .replyMarkup(keyboard)
                    .chatId(id)
                    .build();
        };
    }

    @Override
    public void answerCallbackQuery(CallbackQuery query) {
        inherit.send(new AnswerCallbackQuery(query.getId()));
    }

    @Override
    public void answerCallbackQuery(CallbackQuery query, String userNotification) {
        inherit.send(AnswerCallbackQuery.builder()
                .callbackQueryId(query.getId())
                .text(userNotification)
                .build());
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> void send(Method response) {
        inherit.send(response);
    }

    @Override
    public void send(SendDocument response) {
        inherit.send(response);
    }

    @Override
    public void send(SendPhoto response) {
        inherit.send(response);
    }

    @Override
    public void send(SetWebhook response) {
        inherit.send(response);
    }

    @Override
    public void send(SendVideo response) {
        inherit.send(response);
    }

    @Override
    public void send(SendVideoNote response) {
        inherit.send(response);
    }

    @Override
    public void send(SendSticker response) {
        inherit.send(response);
    }

    @Override
    public void send(SetBusinessAccountProfilePhoto response) {
        inherit.send(response);
    }

    @Override
    public void send(SendAudio response) {
        inherit.send(response);
    }

    @Override
    public void send(SendVoice response) {
        inherit.send(response);
    }

    @Override
    public void send(SendMediaGroup response) {
        inherit.send(response);
    }

    @Override
    public void send(SendPaidMedia response) {
        inherit.send(response);
    }

    @Override
    public void send(SetChatPhoto response) {
        inherit.send(response);
    }

    @Override
    public void send(AddStickerToSet response) {
        inherit.send(response);
    }

    @Override
    public void send(ReplaceStickerInSet response) {
        inherit.send(response);
    }

    @Override
    public void send(SetStickerSetThumbnail response) {
        inherit.send(response);
    }

    @Override
    public void send(CreateNewStickerSet response) {
        inherit.send(response);
    }

    @Override
    public void send(UploadStickerFile response) {
        inherit.send(response);
    }

    @Override
    public void send(EditMessageMedia response) {
        inherit.send(response);
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> sendForObject(Method response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendDocument response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendPhoto response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(SetWebhook response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendVideo response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendVideoNote response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendSticker response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(SetBusinessAccountProfilePhoto response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendAudio response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Message> sendForObject(SendVoice response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<ArrayList<Message>> sendForObject(SendMediaGroup response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<ArrayList<Message>> sendForObject(SendPaidMedia response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(SetChatPhoto response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(AddStickerToSet response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(ReplaceStickerInSet response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(SetStickerSetThumbnail response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Boolean> sendForObject(CreateNewStickerSet response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<org.telegram.telegrambots.meta.api.objects.File> sendForObject(UploadStickerFile response) {
        return inherit.sendForObject(response);
    }

    @Override
    public CompletableFuture<Serializable> sendForObject(EditMessageMedia response) {
        return inherit.sendForObject(response);
    }
}
