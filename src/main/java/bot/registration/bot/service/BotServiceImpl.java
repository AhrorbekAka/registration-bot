package bot.registration.bot.service;

import bot.registration.bot.BotConstant;
import bot.registration.bot.BotState;
import bot.registration.entity.Participant;
import bot.registration.entity.TelegramState;
import bot.registration.entity.enums.CityDistrict;
import bot.registration.entity.enums.ClassNumber;
import bot.registration.repository.ParticipantRepository;
import bot.registration.repository.TelegramStateRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.Optional;

@Service
public class BotServiceImpl implements BotService {

    private final ButtonService myButtons;
    private final TelegramStateRepository tgStateRepo;
    private final ParticipantRepository participantRepo;

    public BotServiceImpl(ButtonService myButtons, TelegramStateRepository tgStateRepo, ParticipantRepository participantRepo) {
        this.myButtons = myButtons;
        this.tgStateRepo = tgStateRepo;
        this.participantRepo = participantRepo;
    }

    @Override
    public SendMessage makeReplyMessage(String text, Long chatId) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        makeMessage(sendMessage, chatId, text);
        return sendMessage;
    }

    private void makeMessage(SendMessage sendMessage, Long chatId, String text) {
        final Optional<Participant> optionalParticipant = participantRepo.findByChatId(chatId);
        if (optionalParticipant.isPresent()) {
            final String state = getStateByChatId(chatId);
            makeMessageByStateAndEditParticipant(sendMessage, state, optionalParticipant.get(), chatId, text);
        } else {
            if (text.equals("/start")) {
                makeMessageAndChangeState(
                        sendMessage,
                        "Registratsiyadan o`tish uchun <strong>Share Contact</strong> tugmasini bosing",
                        chatId,
                        BotState.FIRST_STATE,
                        myButtons.contactButton());
            } else if (text.equals("AsadbekAka2404")) {

            }
        }
    }

    private String getStateByChatId(Long chatId) {
        Optional<TelegramState> stateOptional = tgStateRepo.findByChatId(chatId);
        return stateOptional.isPresent() ? stateOptional.get().getState() : BotState.FIRST_STATE;
    }

    private void makeMessageByStateAndEditParticipant(SendMessage sendMessage, String state, Participant participant, Long chatId, String text) {
        switch (state) {
            case BotState.REGISTERED:
                if (text.equals("/start")) {
                    sendMessage.setText("Siz registratsiyadan o`tgansiz!!!");
                    sendMessage.setReplyMarkup(myButtons.editButton());
                } else if (text.equals(BotState.EDIT)) {
                    makeMessageAndChangeState(sendMessage, "Qaysi ma'lumotni o`zgartirmoqchisiz?", chatId, BotState.EDIT, myButtons.editFieldButtons());
                } else {
                    sendMessage.setText("ExtraRequest");
                }
                break;
            case BotState.LASTNAME:
                if (text.trim().split(" ").length == 1) {
                    if (participant.getLastName().isEmpty()) {
                        makeMessageAndChangeState(sendMessage, "Ismingizni kiriting", chatId, BotState.FIRSTNAME);
                        participant.setLastName(capitalizeText(text));
                    } else {
                        participant.setLastName(capitalizeText(text));
                        notifyAfterEdit(sendMessage, chatId, participant);
                    }
                } else {
                    sendMessage.setText("Familiyangiz uchun faqat bitta so`z kiritishingiz kerak...");
                }
                break;
            case BotState.FIRSTNAME:
                if (text.trim().split(" ").length == 1) {
                    if (participant.getFirstName().isEmpty()) {
                        makeMessageAndChangeState(sendMessage, "Passport ma'lumotlarini kiriting \nNamuna: \nAA 1234567", chatId, BotState.PASSPORT);
                        participant.setFirstName(capitalizeText(text));
                    } else {
                        participant.setFirstName(capitalizeText(text));
                        notifyAfterEdit(sendMessage, chatId, participant);
                    }
                } else {
                    sendMessage.setText("Ismingiz uchun faqat bitta so`z kiritishingiz kerak...");
                }
                break;
            case BotState.PASSPORT:
                text = text.toUpperCase();
                if (checkPassportData(text)) {
                    if (participant.getPassport().isEmpty()) {
                        participant.setPassport(text);
                        String messageText = "Yashaydigan hududingizni tanlang:";
                        makeMessageAndChangeState(sendMessage, messageText, chatId, BotState.CITY_DISTRICT, myButtons.cityDistrictButton());
                    } else {
                        participant.setPassport(text);
                        notifyAfterEdit(sendMessage, chatId, participant);
                    }
                } else {
                    sendMessage.setText("Ma'lumotni noto`g`ri kiritdingiz. Boshqatdan urinib ko`ring");
                }
                break;
            case BotState.CITY_DISTRICT:
                if (text.equals(CityDistrict.ANGREN.name()) || text.equals(CityDistrict.OHANGARON.name())) {
                    if (participant.getCityDistrict() == null) {
                        participant.setCityDistrict(CityDistrict.valueOf(text));
                        String messageText = "O`qiydigan maktabingiz tartib raqamini kiriting:";
                        makeMessageAndChangeState(sendMessage, messageText, chatId, BotState.SCHOOL);
                    } else {
                        participant.setCityDistrict(CityDistrict.valueOf(text));
                        notifyAfterEdit(sendMessage, chatId, participant);
                    }
                } else {
                    sendMessage.setText("Ma'lumotni noto`g`ri kiritdingiz. Boshqatdan urinib ko`ring");
                }
                break;

            case BotState.SCHOOL:
                if (isInteger(text)) {
                    if (participant.getSchoolNumber() == null) {
                        participant.setSchoolNumber(Integer.valueOf((text)));
                        String messageText = "O`qiydigan sinfingiz nomerini tanlang:";
                        makeMessageAndChangeState(sendMessage, messageText, chatId, BotState.CLASS, myButtons.classNumber());
                    } else {
                        participant.setSchoolNumber(Integer.valueOf(text));
                        notifyAfterEdit(sendMessage, chatId, participant);
                    }
                } else {
                    sendMessage.setText("Ma'lumotni noto`g`ri kiritdingiz. Boshqatdan urinib ko`ring");
                }
                break;

            case BotState.CLASS:
                if (text.equals(ClassNumber.TEN.name()) || text.equals(ClassNumber.ELEVEN.name())) {
                    participant.setClassNumber(ClassNumber.valueOf(text));
                    notifyAfterEdit(sendMessage, chatId, participant);
                } else {
                    sendMessage.setText("Ma'lumotni noto`g`ri kiritdingiz. Boshqatdan urinib ko`ring");
                }
                break;

            case BotState.VALIDATE:
                if (text.equals(BotState.VALIDATE)) {
                    makeMessageAndChangeState(sendMessage, "Tabriklayman!!! Registratsiya muvaffaqiyatli yakunlandi", chatId, BotState.REGISTERED);
                    break;
                }
                makeMessageAndChangeState(sendMessage, "Qaysi ma'lumotni o`zgartirmoqchisiz?", chatId, BotState.EDIT, myButtons.editFieldButtons());
                break;
            case BotState.EDIT:
                clearReplyKeyboardTable(sendMessage);

                makeEditBySelectedField(sendMessage, chatId, text, participant);
                break;
            default:
                break;
        }
        participantRepo.save(participant);
    }

    private void makeMessageAndChangeState(SendMessage sendMessage, String text, Long chatId, String state, ReplyKeyboardMarkup replyKeyboardMarkup) {
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        makeMessageAndChangeState(sendMessage, text, chatId, state);
    }

    private void makeMessageAndChangeState(SendMessage sendMessage, String text, Long chatId, String state, InlineKeyboardMarkup inlineKeyboardMarkup) {
        if (inlineKeyboardMarkup != null)
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        makeMessageAndChangeState(sendMessage, text, chatId, state);
    }

    private void makeMessageAndChangeState(SendMessage sendMessage, String text, Long chatId, String state) {
        sendMessage.setText(text);
        changeState(chatId, state);
    }

    public void changeState(Long chatId, String state) {
        Optional<TelegramState> telegramStateOptional = tgStateRepo.findByChatId(chatId);
        if (telegramStateOptional.isPresent()) {
            telegramStateOptional.get().setState(state);
            tgStateRepo.save(telegramStateOptional.get());
        } else {
            tgStateRepo.save(new TelegramState(state, chatId));
        }
    }

    private void notifyAfterEdit(SendMessage sendMessage, Long chatId, Participant participant) {
        makeMessageAndChangeState(
                sendMessage,
                "Ma'lumot saqlandi. Hamma ma'lumot to`g`riligini tasdiqlaysizmi?" +
                        "\n<strong>FI: " + participant.getLastName() + " " + participant.getFirstName() +
                        "\nPassport: " + participant.getPassport() +
                        "\nShahar/Tuman: " + participant.getCityDistrict() +
                        "\nMaktab: " + participant.getSchoolNumber() +
                        "\nSinf: " + (participant.getClassNumber() == ClassNumber.TEN ? 10 : 11) + "\n</strong>",
                chatId, BotState.VALIDATE,
                myButtons.validationButtons());
    }

    private String capitalizeText(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private boolean checkPassportData(String text) {
        String[] words = text.trim().split(" ");

        if (words.length == 2) {
            if (words[0].length() == 2 && words[1].length() == 7) {
                char[] chars = words[0].toCharArray();

                return isInteger(words[1]) && chars[0] == 'A' && (chars[1] == 'A' || chars[1] == 'B');
            }
        }
        return false;
    }

    private boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void clearReplyKeyboardTable(SendMessage sendMessage) {
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
    }

    private void makeEditBySelectedField(SendMessage sendMessage, Long chatId, String text, Participant participant) {
        String messageText = "";
        String newState = BotState.REGISTERED;
        InlineKeyboardMarkup inlineKeyboardMarkup = null;
        switch (text) {
            case BotConstant.EDIT_LASTNAME:
                messageText = "<strong>" + participant.getLastName() + "</strong> -> ro`yhatdan o`tkazilgan familiyangiz. Familiyangizni to`liq yozing";
                newState = BotState.LASTNAME;
                break;
            case BotConstant.EDIT_FIRSTNAME:
                messageText = "<strong>" + participant.getFirstName() + "</strong> -> ro`yhatdan o`tkazilgan ismingiz. Ismingizni to`liq yozing";
                newState = BotState.FIRSTNAME;
                break;
            case BotConstant.EDIT_PASSPORT:
                messageText = "<strong>" + participant.getPassport() + "</strong> -> ro`yhatdan o`tkazilgan seria va raqam. Seria va raqamni to`liq yozing";
                newState = BotState.PASSPORT;
                break;
            case BotConstant.EDIT_CITY_DISTRICT:
                messageText = "<strong>" + participant.getCityDistrict() + "</strong> -> ro`yhatdan o`tkazilgan shahar/tuman. O`zingiz yashaydigan xuddudni tanlang";
                newState = BotState.CITY_DISTRICT;
                inlineKeyboardMarkup = myButtons.cityDistrictButton();
                break;
            case BotConstant.EDIT_SCHOOL:
                messageText = "<strong>" + participant.getSchoolNumber() + "</strong> -> ro`yhatdan o`tkazilgan maktab raqami. Maktabingiz tartib raqamini kiriting";
                newState = BotState.SCHOOL;
                break;
            case BotConstant.EDIT_CLASS:
                messageText = "<strong>" + participant.getClass() + "</strong> -> ro`yhatdan o`tkazilgan sinf. O`zingiz o`qiydigan sinfni tanlang";
                newState = BotState.CLASS;
                inlineKeyboardMarkup = myButtons.classNumber();
                break;
            case BotState.VALIDATE:
            default:
//                            sendMessage.setText("ExtraRequest");
                break;
        }
        makeMessageAndChangeState(sendMessage, messageText, chatId, newState, inlineKeyboardMarkup);
    }


    @Override
    public SendMessage saveParticipantWithContact(Long chatId, String phoneNumber, Integer userId) {
        Optional<Participant> optionalParticipant = participantRepo.findByChatId(chatId);
        if (!optionalParticipant.isPresent()) {
            participantRepo.save(new Participant("", "", "", phoneNumber, userId, chatId, null, null, null));
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Familiyangizni kiriting");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        changeState(chatId, BotState.LASTNAME);
        return sendMessage;
    }
}
