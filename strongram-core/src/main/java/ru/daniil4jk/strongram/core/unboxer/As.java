package ru.daniil4jk.strongram.core.unboxer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.boost.ChatBoostAdded;
import org.telegram.telegrambots.meta.api.objects.boost.ChatBoostRemoved;
import org.telegram.telegrambots.meta.api.objects.boost.ChatBoostUpdated;
import org.telegram.telegrambots.meta.api.objects.business.BusinessConnection;
import org.telegram.telegrambots.meta.api.objects.business.BusinessMessagesDeleted;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.chat.background.ChatBackground;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.forum.*;
import org.telegram.telegrambots.meta.api.objects.games.Animation;
import org.telegram.telegrambots.meta.api.objects.games.Game;
import org.telegram.telegrambots.meta.api.objects.gifts.GiftInfo;
import org.telegram.telegrambots.meta.api.objects.gifts.UniqueGiftInfo;
import org.telegram.telegrambots.meta.api.objects.giveaway.Giveaway;
import org.telegram.telegrambots.meta.api.objects.giveaway.GiveawayCompleted;
import org.telegram.telegrambots.meta.api.objects.giveaway.GiveawayCreated;
import org.telegram.telegrambots.meta.api.objects.giveaway.GiveawayWinners;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.location.Location;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.message.PaidMessagePriceChanged;
import org.telegram.telegrambots.meta.api.objects.messageorigin.MessageOrigin;
import org.telegram.telegrambots.meta.api.objects.passport.PassportData;
import org.telegram.telegrambots.meta.api.objects.payments.*;
import org.telegram.telegrambots.meta.api.objects.payments.paidmedia.PaidMediaInfo;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.api.objects.reactions.MessageReactionCountUpdated;
import org.telegram.telegrambots.meta.api.objects.reactions.MessageReactionUpdated;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.stories.Story;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatEnded;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatParticipantsInvited;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatScheduled;
import org.telegram.telegrambots.meta.api.objects.videochat.VideoChatStarted;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppData;
import ru.daniil4jk.strongram.core.unboxer.finder.text.TextFinderService;

import java.util.List;

/**
 * Static factory that provides pre-built {@link Unboxer} instances for extracting
 * various fields from Telegram {@link Update} objects. Methods fall into two categories:
 * <ul>
 *   <li>Update-scoped extractors — work directly on the {@link Update} (e.g. {@link #callbackQueryData()}, {@link #message()})</li>
 *   <li>Message-scoped extractors — first resolve a {@link org.telegram.telegrambots.meta.api.objects.message.Message} from the update, then extract a field (e.g. {@link #messageText()}, {@link #photo()})</li>
 * </ul>
 */
public class As {
    @Contract(pure = true)
    public static @NotNull Unboxer<Update> update() {
        return update -> update;
    }

    @Contract(pure = true)
    public static @NotNull Unboxer<String> text() {
        return update -> TextFinderService.getInstance().findIn(update);
    }

    public static @NotNull Unboxer<Message> message() {
        return Update::getMessage;
    }

    public static @NotNull Unboxer<String> messageText() {
        return message().andThen(message -> {
            if (message.hasText()) return message.getText();
            if (message.hasCaption()) return message.getCaption();
            return null;
        });
    }

    public static @NotNull Unboxer<Integer> date() {
        return message().andThen(Message::getDate);
    }

    public static @NotNull Unboxer<User> forwardFrom() {
        return message().andThen(Message::getForwardFrom);
    }

    public static @NotNull Unboxer<Chat> forwardFromChat() {
        return message().andThen(Message::getForwardFromChat);
    }

    public static @NotNull Unboxer<Integer> forwardDate() {
        return message().andThen(Message::getForwardDate);
    }

    public static @NotNull Unboxer<Contact> contact() {
        return message().andThen(Message::getContact);
    }

    public static @NotNull Unboxer<Location> location() {
        return message().andThen(Message::getLocation);
    }

    public static @NotNull Unboxer<Venue> venue() {
        return message().andThen(Message::getVenue);
    }

    public static @NotNull Unboxer<Animation> animation() {
        return message().andThen(Message::getAnimation);
    }

    public static @NotNull Unboxer<MaybeInaccessibleMessage> pinnedMessage() {
        return message().andThen(Message::getPinnedMessage);
    }

    public static @NotNull Unboxer<User> leftChatMember() {
        return message().andThen(Message::getLeftChatMember);
    }

    public static @NotNull Unboxer<String> newChatTitle() {
        return message().andThen(Message::getNewChatTitle);
    }

    public static @NotNull Unboxer<List<PhotoSize>> newChatPhoto() {
        return message().andThen(Message::getNewChatPhoto);
    }

    public static @NotNull Unboxer<Boolean> deleteChatPhoto() {
        return message().andThen(Message::getDeleteChatPhoto);
    }

    public static @NotNull Unboxer<Boolean> groupchatCreated() {
        return message().andThen(Message::getGroupchatCreated);
    }

    public static @NotNull Unboxer<Message> replyToMessage() {
        return message().andThen(Message::getReplyToMessage);
    }

    public static @NotNull Unboxer<Voice> voice() {
        return message().andThen(Message::getVoice);
    }

    public static @NotNull Unboxer<String> caption() {
        return message().andThen(Message::getCaption);
    }

    public static @NotNull Unboxer<Boolean> superGroupCreated() {
        return message().andThen(Message::getSuperGroupCreated);
    }

    public static @NotNull Unboxer<Boolean> channelChatCreated() {
        return message().andThen(Message::getChannelChatCreated);
    }

    public static @NotNull Unboxer<Long> migrateToChatId() {
        return message().andThen(Message::getMigrateToChatId);
    }

    public static @NotNull Unboxer<Long> migrateFromChatId() {
        return message().andThen(Message::getMigrateFromChatId);
    }

    public static @NotNull Unboxer<Integer> editDate() {
        return message().andThen(Message::getEditDate);
    }

    public static @NotNull Unboxer<Game> game() {
        return message().andThen(Message::getGame);
    }

    public static @NotNull Unboxer<Integer> forwardFromMessageId() {
        return message().andThen(Message::getForwardFromMessageId);
    }

    public static @NotNull Unboxer<Invoice> invoice() {
        return message().andThen(Message::getInvoice);
    }

    public static @NotNull Unboxer<SuccessfulPayment> successfulPayment() {
        return message().andThen(Message::getSuccessfulPayment);
    }

    public static @NotNull Unboxer<VideoNote> videoNote() {
        return message().andThen(Message::getVideoNote);
    }

    public static @NotNull Unboxer<String> authorSignature() {
        return message().andThen(Message::getAuthorSignature);
    }

    public static @NotNull Unboxer<String> forwardSignature() {
        return message().andThen(Message::getForwardSignature);
    }

    public static @NotNull Unboxer<String> mediaGroupId() {
        return message().andThen(Message::getMediaGroupId);
    }

    public static @NotNull Unboxer<String> connectedWebsite() {
        return message().andThen(Message::getConnectedWebsite);
    }

    public static @NotNull Unboxer<PassportData> passportData() {
        return message().andThen(Message::getPassportData);
    }

    public static @NotNull Unboxer<String> forwardSenderName() {
        return message().andThen(Message::getForwardSenderName);
    }

    public static @NotNull Unboxer<Poll> messagePoll() {
        return message().andThen(Message::getPoll);
    }

    public static @NotNull Unboxer<InlineKeyboardMarkup> replyMarkup() {
        return message().andThen(Message::getReplyMarkup);
    }

    public static @NotNull Unboxer<Dice> dice() {
        return message().andThen(Message::getDice);
    }

    public static @NotNull Unboxer<User> viaBot() {
        return message().andThen(Message::getViaBot);
    }

    public static @NotNull Unboxer<Chat> senderChat() {
        return message().andThen(Message::getSenderChat);
    }

    public static @NotNull Unboxer<ProximityAlertTriggered> proximityAlertTriggered() {
        return message().andThen(Message::getProximityAlertTriggered);
    }

    public static @NotNull Unboxer<MessageAutoDeleteTimerChanged> messageAutoDeleteTimerChanged() {
        return message().andThen(Message::getMessageAutoDeleteTimerChanged);
    }

    public static @NotNull Unboxer<Boolean> isAutomaticForward() {
        return message().andThen(Message::getIsAutomaticForward);
    }

    public static @NotNull Unboxer<Boolean> hasProtectedContent() {
        return message().andThen(Message::getHasProtectedContent);
    }

    public static @NotNull Unboxer<WebAppData> webAppData() {
        return message().andThen(Message::getWebAppData);
    }

    public static @NotNull Unboxer<VideoChatStarted> videoChatStarted() {
        return message().andThen(Message::getVideoChatStarted);
    }

    public static @NotNull Unboxer<VideoChatEnded> videoChatEnded() {
        return message().andThen(Message::getVideoChatEnded);
    }

    public static @NotNull Unboxer<VideoChatParticipantsInvited> videoChatParticipantsInvited() {
        return message().andThen(Message::getVideoChatParticipantsInvited);
    }

    public static @NotNull Unboxer<VideoChatScheduled> videoChatScheduled() {
        return message().andThen(Message::getVideoChatScheduled);
    }

    public static @NotNull Unboxer<Boolean> isTopicMessage() {
        return message().andThen(Message::getIsTopicMessage);
    }

    public static @NotNull Unboxer<ForumTopicCreated> forumTopicCreated() {
        return message().andThen(Message::getForumTopicCreated);
    }

    public static @NotNull Unboxer<ForumTopicClosed> forumTopicClosed() {
        return message().andThen(Message::getForumTopicClosed);
    }

    public static @NotNull Unboxer<ForumTopicReopened> forumTopicReopened() {
        return message().andThen(Message::getForumTopicReopened);
    }

    public static @NotNull Unboxer<ForumTopicEdited> forumTopicEdited() {
        return message().andThen(Message::getForumTopicEdited);
    }

    public static @NotNull Unboxer<GeneralForumTopicHidden> generalForumTopicHidden() {
        return message().andThen(Message::getGeneralForumTopicHidden);
    }

    public static @NotNull Unboxer<GeneralForumTopicUnhidden> generalForumTopicUnhidden() {
        return message().andThen(Message::getGeneralForumTopicUnhidden);
    }

    public static @NotNull Unboxer<WriteAccessAllowed> writeAccessAllowed() {
        return message().andThen(Message::getWriteAccessAllowed);
    }

    public static @NotNull Unboxer<Boolean> hasMediaSpoiler() {
        return message().andThen(Message::getHasMediaSpoiler);
    }

    public static @NotNull Unboxer<UserShared> userShared() {
        return message().andThen(Message::getUserShared);
    }

    public static @NotNull Unboxer<ChatShared> chatShared() {
        return message().andThen(Message::getChatShared);
    }

    public static @NotNull Unboxer<Story> story() {
        return message().andThen(Message::getStory);
    }

    public static @NotNull Unboxer<ExternalReplyInfo> externalReplyInfo() {
        return message().andThen(Message::getExternalReplyInfo);
    }

    public static @NotNull Unboxer<MessageOrigin> forwardOrigin() {
        return message().andThen(Message::getForwardOrigin);
    }

    public static @NotNull Unboxer<LinkPreviewOptions> linkPreviewOptions() {
        return message().andThen(Message::getLinkPreviewOptions);
    }

    public static @NotNull Unboxer<TextQuote> quote() {
        return message().andThen(Message::getQuote);
    }

    public static @NotNull Unboxer<UsersShared> usersShared() {
        return message().andThen(Message::getUsersShared);
    }

    public static @NotNull Unboxer<GiveawayCreated> giveawayCreated() {
        return message().andThen(Message::getGiveawayCreated);
    }

    public static @NotNull Unboxer<Giveaway> giveaway() {
        return message().andThen(Message::getGiveaway);
    }

    public static @NotNull Unboxer<GiveawayWinners> giveawayWinners() {
        return message().andThen(Message::getGiveawayWinners);
    }

    public static @NotNull Unboxer<GiveawayCompleted> giveawayCompleted() {
        return message().andThen(Message::getGiveawayCompleted);
    }

    public static @NotNull Unboxer<Story> replyToStory() {
        return message().andThen(Message::getReplyToStory);
    }

    public static @NotNull Unboxer<ChatBoostAdded> boostAdded() {
        return message().andThen(Message::getBoostAdded);
    }

    public static @NotNull Unboxer<Integer> senderBoostCount() {
        return message().andThen(Message::getSenderBoostCount);
    }

    public static @NotNull Unboxer<String> businessConnectionId() {
        return message().andThen(Message::getBusinessConnectionId);
    }

    public static @NotNull Unboxer<User> senderBusinessBot() {
        return message().andThen(Message::getSenderBusinessBot);
    }

    public static @NotNull Unboxer<Boolean> isFromOffline() {
        return message().andThen(Message::getIsFromOffline);
    }

    public static @NotNull Unboxer<ChatBackground> chatBackgroundSet() {
        return message().andThen(Message::getChatBackgroundSet);
    }

    public static @NotNull Unboxer<String> effectId() {
        return message().andThen(Message::getEffectId);
    }

    public static @NotNull Unboxer<Boolean> showCaptionAboveMedia() {
        return message().andThen(Message::getShowCaptionAboveMedia);
    }

    public static @NotNull Unboxer<PaidMediaInfo> paidMedia() {
        return message().andThen(Message::getPaidMedia);
    }

    public static @NotNull Unboxer<RefundedPayment> refundedPayment() {
        return message().andThen(Message::getRefundedPayment);
    }

    public static @NotNull Unboxer<GiftInfo> gift() {
        return message().andThen(Message::getGift);
    }

    public static @NotNull Unboxer<UniqueGiftInfo> uniqueGift() {
        return message().andThen(Message::getUniqueGift);
    }

    public static @NotNull Unboxer<PaidMessagePriceChanged> paidMessagePriceChanged() {
        return message().andThen(Message::getPaidMessagePriceChanged);
    }

    public static @NotNull Unboxer<Integer> paidStarCount() {
        return message().andThen(Message::getPaidStarCount);
    }

    public static @NotNull Unboxer<InlineQuery> inlineQuery() {
        return Update::getInlineQuery;
    }

    public static @NotNull Unboxer<ChosenInlineQuery> chosenInlineQuery() {
        return Update::getChosenInlineQuery;
    }

    public static @NotNull Unboxer<CallbackQuery> callbackQuery() {
        return Update::getCallbackQuery;
    }

    public static @NotNull Unboxer<String> callbackQueryData() {
        return callbackQuery().andThen(CallbackQuery::getData);
    }

    public static @NotNull Unboxer<Message> editedMessage() {
        return Update::getEditedMessage;
    }

    public static @NotNull Unboxer<Message> channelPost() {
        return Update::getChannelPost;
    }

    public static @NotNull Unboxer<Message> editedChannelPost() {
        return Update::getEditedChannelPost;
    }

    public static @NotNull Unboxer<ShippingQuery> shippingQuery() {
        return Update::getShippingQuery;
    }

    public static @NotNull Unboxer<PreCheckoutQuery> preCheckoutQuery() {
        return Update::getPreCheckoutQuery;
    }

    public static @NotNull Unboxer<Poll> poll() {
        return Update::getPoll;
    }

    public static @NotNull Unboxer<PollAnswer> pollAnswer() {
        return Update::getPollAnswer;
    }

    public static @NotNull Unboxer<ChatMemberUpdated> myChatMember() {
        return Update::getMyChatMember;
    }

    public static @NotNull Unboxer<ChatMemberUpdated> chatMember() {
        return Update::getChatMember;
    }

    public static @NotNull Unboxer<ChatJoinRequest> chatJoinRequest() {
        return Update::getChatJoinRequest;
    }

    public static @NotNull Unboxer<MessageReactionUpdated> messageReaction() {
        return Update::getMessageReaction;
    }

    public static @NotNull Unboxer<MessageReactionCountUpdated> messageReactionCount() {
        return Update::getMessageReactionCount;
    }

    public static @NotNull Unboxer<ChatBoostUpdated> chatBoost() {
        return Update::getChatBoost;
    }

    public static @NotNull Unboxer<ChatBoostRemoved> removedChatBoost() {
        return Update::getRemovedChatBoost;
    }

    public static @NotNull Unboxer<BusinessConnection> businessConnection() {
        return Update::getBusinessConnection;
    }

    public static @NotNull Unboxer<Message> businessMessage() {
        return Update::getBusinessMessage;
    }

    public static @NotNull Unboxer<Message> editedBusinessMessage() {
        return Update::getEditedBuinessMessage;
    }

    public static @NotNull Unboxer<BusinessMessagesDeleted> deletedBusinessMessages() {
        return Update::getDeletedBusinessMessages;
    }

    public static @NotNull Unboxer<PaidMediaPurchased> paidMediaPurchased() {
        return Update::getPaidMediaPurchased;
    }
}
