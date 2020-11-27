package bot.registration.bot.service;

import bot.registration.bot.BotConstant;
import bot.registration.bot.BotState;
import bot.registration.entity.enums.CityDistrict;
import bot.registration.entity.enums.ClassNumber;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class ButtonService {

    public ReplyKeyboardMarkup contactButton(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText("Share contact");
        button.setRequestContact(true);
        row.add(button);
        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup editButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText(BotState.EDIT);
        row.add(button);
        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup editFieldButtons(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttonsList=  new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        InlineKeyboardButton button5 = new InlineKeyboardButton();

        button.setText("Ism");
        button.setCallbackData(BotConstant.EDIT_FIRSTNAME);

        button1.setText("Familiya");
        button1.setCallbackData(BotConstant.EDIT_LASTNAME);

        button2.setText("Passport");
        button2.setCallbackData(BotConstant.EDIT_PASSPORT);

        buttonsList.add(button);
        buttonsList.add(button1);
        buttonsList.add(button2);

        rowList.add(buttonsList);

        button3.setText("Shahar/Tuman");
        button3.setCallbackData(BotConstant.EDIT_CITY_DISTRICT);

        button4.setText("Maktab");
        button4.setCallbackData(BotConstant.EDIT_SCHOOL);

        button5.setText("Sinif");
        button5.setCallbackData(BotConstant.EDIT_CLASS);

        buttonsList = new ArrayList<>();
        buttonsList.add(button3);
        buttonsList.add(button4);
        buttonsList.add(button5);

        rowList.add(buttonsList);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup validationButtons(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttonsList=  new ArrayList<>();

        InlineKeyboardButton buttonValidate = new InlineKeyboardButton();
        InlineKeyboardButton buttonEdit = new InlineKeyboardButton();

        buttonEdit.setText("Tahrirlash ✍️");
        buttonEdit.setCallbackData(BotState.EDIT);

        buttonValidate.setText("Tasdiqlash ✅");
        buttonValidate.setCallbackData(BotState.VALIDATE);

        buttonsList.add(buttonEdit);
        buttonsList.add(buttonValidate);
        rowList.add(buttonsList);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public InlineKeyboardMarkup cityDistrictButton(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttonsList=  new ArrayList<>();

        InlineKeyboardButton buttonCity = new InlineKeyboardButton();
        InlineKeyboardButton buttonDistrict = new InlineKeyboardButton();

        buttonCity.setText(CityDistrict.ANGREN.name());
        buttonCity.setCallbackData(CityDistrict.ANGREN.name());

        buttonDistrict.setText(CityDistrict.OHANGARON.name());
        buttonDistrict.setCallbackData(CityDistrict.OHANGARON.name());

        buttonsList.add(buttonCity);
        buttonsList.add(buttonDistrict);
        rowList.add(buttonsList);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup classNumber(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> buttonsList=  new ArrayList<>();

        InlineKeyboardButton buttonTen = new InlineKeyboardButton();
        InlineKeyboardButton buttonEleven = new InlineKeyboardButton();

        buttonTen.setText("10");
        buttonTen.setCallbackData(ClassNumber.TEN.name());

        buttonEleven.setText("11");
        buttonEleven.setCallbackData(ClassNumber.ELEVEN.name());

        buttonsList.add(buttonTen);
        buttonsList.add(buttonEleven);
        rowList.add(buttonsList);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup myButton(String text,String query){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsList=  new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(query);
        buttonsList.add(button);
        rows1.add(buttonsList);
        inlineKeyboardMarkup.setKeyboard(rows1);
        return inlineKeyboardMarkup;
    }

//    public InlineKeyboardMarkup oneRowButtons(List<ReqOneRowButtons> reqOneRowButtons){
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows1 = new ArrayList<>();
//        List<InlineKeyboardButton> buttonsList=  new ArrayList<>();
//        for (ReqOneRowButtons reqOneRowButton : reqOneRowButtons) {
//            InlineKeyboardButton button = new InlineKeyboardButton();
//            button.setText(reqOneRowButton.getText());
//            button.setCallbackData(reqOneRowButton.getQuery());
//            buttonsList.add(button);
//        }
//        rows1.add(buttonsList);
//        inlineKeyboardMarkup.setKeyboard(rows1);
//        return inlineKeyboardMarkup;
//    }

}
