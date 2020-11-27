package bot.registration.bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotService {

    SendMessage makeReplyMessage(String text, Long chatId);

    SendMessage saveParticipantWithContact(Long chatId, String phoneNumber, Integer userId);
}
