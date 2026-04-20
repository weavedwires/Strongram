package ru.daniil4jk.strongram.core.filter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.daniil4jk.strongram.core.context.request.RequestContext;
import ru.daniil4jk.strongram.core.context.request.TelegramUUID;
import ru.daniil4jk.strongram.core.unboxer.As;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Filters {
    private Filters() {
    }

    @Contract(pure = true)
    public static @NotNull Filter acceptAll() {
        return update -> true;
    }

    @Contract(pure = true)
    public static @NotNull Filter rejectAll() {
        return update -> false;
    }

    @Contract(pure = true)
    public static @NotNull Filter hasMessage() {
        return ctx -> upd(ctx).hasMessage();
    }

    public static @NotNull Filter hasText() {
        return ctx -> {
            try {
                str(ctx);
                return true;
            } catch (Exception e) {
                return false;
            }
        };
    }

    public static @NotNull Filter hasMessageText() {
        return hasMessage().and(ctx -> msg(ctx).hasText());
    }

    public static @NotNull Filter hasPhoto() {
        return hasMessage().and(ctx -> msg(ctx).hasPhoto());
    }

    public static @NotNull Filter hasDocument() {
        return hasMessage().and(ctx -> msg(ctx).hasDocument());
    }

    public static @NotNull Filter hasSticker() {
        return hasMessage().and(ctx -> msg(ctx).hasSticker());
    }

    public static @NotNull Filter hasVideo() {
        return hasMessage().and(ctx -> msg(ctx).hasVideo());
    }

    public static @NotNull Filter hasAudio() {
        return hasMessage().and(ctx -> msg(ctx).hasAudio());
    }

    public static @NotNull Filter hasVoice() {
        return hasMessage().and(ctx -> msg(ctx).hasVoice());
    }

    public static @NotNull Filter hasVideoNote() {
        return hasMessage().and(ctx -> msg(ctx).hasVideoNote());
    }

    public static @NotNull Filter hasContact() {
        return hasMessage().and(ctx -> msg(ctx).hasContact());
    }

    public static @NotNull Filter hasLocation() {
        return hasMessage().and(ctx -> msg(ctx).hasLocation());
    }

    public static @NotNull Filter hasAnimation() {
        return hasMessage().and(ctx -> msg(ctx).hasAnimation());
    }

    public static @NotNull Filter hasGame() {
        return hasMessage().and(ctx -> msg(ctx).hasGame());
    }

    public static @NotNull Filter hasPollInMessage() {
        return hasMessage().and(ctx -> msg(ctx).hasPoll());
    }

    public static @NotNull Filter hasDice() {
        return hasMessage().and(ctx -> msg(ctx).hasDice());
    }

    public static @NotNull Filter hasInvoice() {
        return hasMessage().and(ctx -> msg(ctx).hasInvoice());
    }

    public static @NotNull Filter hasSuccessfulPayment() {
        return hasMessage().and(ctx -> msg(ctx).hasSuccessfulPayment());
    }

    public static @NotNull Filter hasPassportData() {
        return hasMessage().and(ctx -> msg(ctx).hasPassportData());
    }

    public static @NotNull Filter hasCaption() {
        return hasMessage().and(ctx -> msg(ctx).hasCaption());
    }

    public static @NotNull Filter hasEntities() {
        return hasMessage().and(ctx -> msg(ctx).hasEntities());
    }

    public static @NotNull Filter isCommand() {
        return hasMessage().and(ctx -> msg(ctx).isCommand());
    }

    public static @NotNull Filter isReply() {
        return hasMessage().and(ctx -> msg(ctx).isReply());
    }

    public static @NotNull Filter hasViaBot() {
        return hasMessage().and(ctx -> msg(ctx).hasViaBot());
    }

    public static @NotNull Filter hasReplyMarkup() {
        return hasMessage().and(ctx -> msg(ctx).hasReplyMarkup());
    }

    public static @NotNull Filter hasMessageAutoDeleteTimerChanged() {
        return hasMessage().and(ctx -> msg(ctx).hasMessageAutoDeleteTimerChanged());
    }

    public static @NotNull Filter hasVideoChatStarted() {
        return hasMessage().and(ctx -> msg(ctx).hasVideoChatStarted());
    }

    public static @NotNull Filter hasVideoChatEnded() {
        return hasMessage().and(ctx -> msg(ctx).hasVideoChatEnded());
    }

    public static @NotNull Filter hasVideoChatScheduled() {
        return hasMessage().and(ctx -> msg(ctx).hasVideoChatScheduled());
    }

    public static @NotNull Filter hasVideoChatParticipantsInvited() {
        return hasMessage().and(ctx -> msg(ctx).hasVideoChatParticipantsInvited());
    }

    // Фильтры для форумов
    public static @NotNull Filter isTopicMessage() {
        return hasMessage().and(ctx -> msg(ctx).isTopicMessage());
    }

    public static @NotNull Filter hasForumTopicCreated() {
        return hasMessage().and(ctx -> msg(ctx).hasForumTopicCreated());
    }

    public static @NotNull Filter hasForumTopicClosed() {
        return hasMessage().and(ctx -> msg(ctx).hasForumTopicClosed());
    }

    public static @NotNull Filter hasForumTopicReopened() {
        return hasMessage().and(ctx -> msg(ctx).hasForumTopicReopened());
    }

    public static @NotNull Filter hasUserShared() {
        return hasMessage().and(ctx -> msg(ctx).hasUserShared());
    }

    public static @NotNull Filter hasChatShared() {
        return hasMessage().and(ctx -> msg(ctx).hasChatShared());
    }

    public static @NotNull Filter hasStory() {
        return hasMessage().and(ctx -> msg(ctx).hasStory());
    }

    public static @NotNull Filter hasReplyToStory() {
        return hasMessage().and(ctx -> msg(ctx).hasReplyToStory());
    }

    public static @NotNull Filter hasWebAppData() {
        return hasMessage().and(ctx -> msg(ctx).hasWebAppData());
    }

    public static @NotNull Filter hasWriteAccessAllowed() {
        return hasMessage().and(ctx -> msg(ctx).hasWriteAccessAllowed());
    }

    public static @NotNull Filter hasBoostAdded() {
        return hasMessage().and(ctx -> msg(ctx).hasBoostAdded());
    }

    public static @NotNull Filter hasGift() {
        return hasMessage().and(ctx -> msg(ctx).hasGift());
    }

    public static @NotNull Filter hasUniqueGift() {
        return hasMessage().and(ctx -> msg(ctx).hasUniqueGift());
    }

    public static @NotNull Filter hasPaidMedia() {
        return hasMessage().and(ctx -> msg(ctx).hasPaidMedia());
    }

    public static @NotNull Filter hasPaidMessagePriceChanged() {
        return hasMessage().and(ctx -> msg(ctx).hasPaidMessagePriceChanged());
    }

    public static @NotNull Filter hasPaidStarCount() {
        return hasMessage().and(ctx -> msg(ctx).hasPaidStarCount());
    }

    public static @NotNull Filter isPrivateChat() {
        return hasMessage().and(ctx -> msg(ctx).isUserMessage());
    }

    public static @NotNull Filter isGroupChat() {
        return hasMessage().and(ctx -> msg(ctx).isGroupMessage());
    }

    public static @NotNull Filter isSuperGroupChat() {
        return hasMessage().and(ctx -> msg(ctx).isSuperGroupMessage());
    }

    public static @NotNull Filter isChannelChat() {
        return hasMessage().and(ctx -> msg(ctx).isChannelMessage());
    }

    @Contract(pure = true)
    public static @NotNull Filter hasInlineQuery() {
        return ctx -> upd(ctx).hasInlineQuery();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasChosenInlineQuery() {
        return ctx -> upd(ctx).hasChosenInlineQuery();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasCallbackQuery() {
        return ctx -> upd(ctx).hasCallbackQuery();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasEditedMessage() {
        return ctx -> upd(ctx).hasEditedMessage();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasChannelPost() {
        return ctx -> upd(ctx).hasChannelPost();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasEditedChannelPost() {
        return ctx -> upd(ctx).hasEditedChannelPost();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasBusinessConnection() {
        return ctx -> upd(ctx).hasBusinessConnection();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasBusinessMessage() {
        return ctx -> upd(ctx).hasBusinessMessage();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasEditedBusinessMessage() {
        return ctx -> upd(ctx).hasEditedBusinessMessage();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasDeletedBusinessMessage() {
        return ctx -> upd(ctx).hasDeletedBusinessMessage();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasShippingQuery() {
        return ctx -> upd(ctx).hasShippingQuery();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasPreCheckoutQuery() {
        return ctx -> upd(ctx).hasPreCheckoutQuery();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasPaidMediaPurchased() {
        return ctx -> upd(ctx).hasPaidMediaPurchased();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasPoll() {
        return ctx -> upd(ctx).hasPoll();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasPollAnswer() {
        return ctx -> upd(ctx).hasPollAnswer();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasMyChatMember() {
        return ctx -> upd(ctx).hasMyChatMember();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasChatMember() {
        return ctx -> upd(ctx).hasChatMember();
    }

    @Contract(pure = true)
    public static @NotNull Filter hasChatJoinRequest() {
        return ctx -> upd(ctx).hasChatJoinRequest();
    }

    public static @NotNull Filter userIdIs(Long id) {
        return ctx -> ctx.getUUID().user().getId().equals(id);
    }

    public static @NotNull Filter chatIdIs(Long id) {
        return ctx -> ctx.getUUID().chat().getId().equals(id);
    }

    public static @NotNull Filter textStartWith(String text) {
        return ctx -> str(ctx).startsWith(text);
    }

    public static @NotNull Filter textEndsWith(String text) {
        return ctx -> str(ctx).endsWith(text);
    }

    public static @NotNull Filter textContains(String text) {
        return ctx -> str(ctx).contains(text);
    }

    public static @NotNull Filter textContainsIgnoreCase(String text) {
        return ctx -> str(ctx).toLowerCase().contains(text.toLowerCase());
    }

    public static @NotNull Filter textEquals(String text) {
        return ctx -> str(ctx).equals(text);
    }

    public static @NotNull Filter textEqualsIgnoreCase(String text) {
        return ctx -> str(ctx).equalsIgnoreCase(text);
    }

    @SafeVarargs
    public static <T> @NotNull Filter iterateOr(Function<T, Filter> filter, T t, @Nullable T... ts) {
        return iterate(filter, Filter::or, t, ts);
    }

    @SafeVarargs
    public static <T> @NotNull Filter iterateAnd(Function<T, Filter> filter, T t, @Nullable T... ts) {
        return iterate(filter, Filter::and, t, ts);
    }

    @SafeVarargs
    private static <T> @NotNull Filter iterate(Function<T, Filter> converter,
                                               BiFunction<Filter, Filter, Filter> strategy,
                                               T t, @Nullable T... ts) {
        Filter filter = converter.apply(t);
        if (ts == null) {
            return filter;
        }

        for (T i : ts) {
            filter = strategy.apply(filter, converter.apply(t));
        }
        return filter;
    }

    public static @NotNull Filter hasUser() {
        return ctx -> ctx.getUUID().user() != null;
    }

    public static @NotNull Filter hasChat() {
        return ctx -> ctx.getUUID().chat() != null;
    }

    public static @NotNull Filter hasReplyChatId() {
        return ctx -> ctx.getUUID().chat() != null;
    }

    public static @NotNull Filter uuidEquals(TelegramUUID anotherUuid) {
        return ctx -> Objects.equals(ctx.getUUID(), anotherUuid);
    }

    private static Update upd(@NotNull RequestContext ctx) {
        return ctx.getRequest();
    }

    private static Message msg(@NotNull RequestContext ctx) {
        return ctx.getRequest(As.message());
    }

    private static String str(RequestContext ctx) {
        return ctx.getRequest(As.text());
    }
}