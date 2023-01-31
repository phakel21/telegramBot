package com.bot.coolbotdemo.service;

import com.bot.coolbotdemo.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        button(update);

//        System.out.println("Hi");
    }

    private void button(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {

                String text = message.getText();
                if (text.equals("/start")) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Welcome " + message.getChat().getFirstName());
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    sendMessage.setChatId(message.getChatId());
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    List<KeyboardRow> keyboardRowList = new ArrayList<>();
                    KeyboardRow keyboardRow = new KeyboardRow();
                    KeyboardButton keyboardButton = new KeyboardButton();
                    keyboardButton.setText("Get Image");
                    keyboardRow.add(keyboardButton);
                    keyboardRowList.add(keyboardRow);
                    replyKeyboardMarkup.setKeyboard(keyboardRowList);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (text.equals("Get Image")) {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(message.getChatId());
                    sendPhoto.setPhoto(new InputFile(new File("E:/scorpion.PNG")));
                    try {
                        execute(sendPhoto);

                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
//        SendMessage sendMessage = new SendMessage();
//        Long chatId = update.getMessage().getChatId();
//
//        if (update.hasMessage()) {
//            if (update.getMessage().getText().equals("/start")) {
//                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//                List<KeyboardRow> keyboardRowList = new ArrayList<>();
//                KeyboardRow keyboardRow = new KeyboardRow();
//                KeyboardButton keyboardButton = new KeyboardButton();
//                keyboardButton.setText("Nude");
//                keyboardRow.add(keyboardButton);
//                keyboardRowList.add(keyboardRow);
//                replyKeyboardMarkup.setKeyboard(keyboardRowList);
//                sendMessage.setReplyMarkup(replyKeyboardMarkup);
//                sendMessage.setChatId(chatId);
//            }
//        }

    }

    private void write(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            switch (message) {
                case "/start":
                    start(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    response(chatId, "Default");
            }
        }
    }

    private void start(Long chatId, String firstName) {
        String response = "Hello, " + firstName;

        response(chatId, response);
    }

    private void response(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);


//        SendPhoto sendPhoto = new SendPhoto();
//        sendPhoto.setChatId(chatId);
//        sendPhoto.setPhoto(new InputFile(new File("E:/scorpion.png")));

        try {
            execute(sendMessage);
//            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
