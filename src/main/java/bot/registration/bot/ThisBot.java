package bot.registration.bot;

import bot.registration.bot.service.BotService;
import bot.registration.bot.service.ButtonService;
import bot.registration.entity.TelegramMessage;
import bot.registration.repository.TelegramMessageRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Component
public class ThisBot extends TelegramLongPollingBot {

    private final BotService botService;
    private final TelegramMessageRepository messageRepo;

    public ThisBot(BotService botService, TelegramMessageRepository messageRepo) {
        this.botService = botService;
        this.messageRepo = messageRepo;
    }

    @Override
    public String getBotUsername() {
        return "TesterBootbot";
    }

    @Override
    public String getBotToken() {
        return "1173193780:AAHooDeioFSezq-yQ_rDpm_M5Y_bke57008";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();

            if (update.getMessage().hasText()) {
                String text = update.getMessage().getText();

                SendMessage sendMessage = botService.makeReplyMessage(text, chatId);
                if (!sendMessage.getText().equals("ExtraRequest")) {
                    send(sendMessage);
                } else {
                    deleteMessage(chatId, update.getMessage().getMessageId());
                }

            } else if (update.getMessage().hasContact()) {
                Contact contact = update.getMessage().getContact();
                send(botService.saveParticipantWithContact(chatId, contact.getPhoneNumber(), contact.getUserID()));
            }
        } else if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            send(botService.makeReplyMessage(callbackQuery.getData(), callbackQuery.getMessage().getChatId()));
        }
    }

    public void send(SendMessage sendMessage) {
        try {
            sendMessage.setParseMode(ParseMode.HTML);
            org.telegram.telegrambots.meta.api.objects.Message message = execute(sendMessage);
//            messageRepo.save(new TelegramMessage(Long.parseLong(sendMessage.getChatId()), message.getMessageId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(Long chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        if (messageId == null) {
            Optional<List<TelegramMessage>> messages = messageRepo.findAllByChatId(chatId);
            if (messages.isPresent()) {
                messageId = messages.get().get(0).getMessageId();
                try {
                    messageRepo.deleteAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else return;
        }
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        executeDelete(deleteMessage);
    }

    private void executeDelete(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
