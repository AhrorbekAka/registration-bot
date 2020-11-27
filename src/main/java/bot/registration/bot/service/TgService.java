package bot.registration.bot.service;

import bot.registration.bot.BotState;
import bot.registration.bot.ThisBot;
import bot.registration.entity.Participant;
import bot.registration.entity.TelegramState;
import bot.registration.repository.ParticipantRepository;
import bot.registration.repository.TelegramStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class TgService {

//    private final TelegramStateRepository telegramStateRepository;
//    private final ButtonService buttonService;
//    private final ThisBot thisBot;
////    private final ParticipantRepository participantRepo;
//
//    public TgService(TelegramStateRepository telegramStateRepository, ButtonService buttonService, ThisBot thisBot, ParticipantRepository participantRepo){
//        this.telegramStateRepository = telegramStateRepository;
//        this.buttonService = buttonService;
//        this.thisBot = thisBot;
////        this.participantRepo = participantRepo;
//    }
//
//    public SendMessage homePage(Long chatId){
//        changeState(chatId, BotState.REGISTERED);
//        thisBot.delete(chatId);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("<b>********Home page********</b>");
//        sendMessage.setChatId(String.valueOf(chatId));
//        sendMessage.setParseMode(ParseMode.HTML);
//        sendMessage.setReplyMarkup(buttonService.homePageButtons());
//        return sendMessage;
//    }
//
//    public void changeState(Long chatId,String state){
//        Optional<TelegramState> telegramStateOptional = telegramStateRepository.findByTgId(chatId.intValue());
//        if(telegramStateOptional.isPresent()){
//            telegramStateOptional.get().setState(state);
//            telegramStateRepository.save(telegramStateOptional.get());
//        }else{
//            telegramStateRepository.save(new TelegramState(state,chatId.intValue()));
//        }
//    }
//
//    public void onOutRule(Update req,String text){
//        thisBot.deleteClientMessage(req);
//        SendMessage sendMessage= new SendMessage();
//        sendMessage.setText(text);
//        sendMessage.setChatId(String.valueOf(req.getMessage().getChatId()));
//        thisBot.send(sendMessage);
//    }
//


}
