package com.javarush.telegram;

import com.javarush.telegram.ChatGPTService;
import com.javarush.telegram.DialogMode;
import com.javarush.telegram.MultiSessionTelegramBot;
import com.javarush.telegram.UserInfo;
import com.plexpt.chatgpt.ChatGPT;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = "Tinder_this_ai_bot"; //TODO: добавь имя бота в кавычках
    public static final String TELEGRAM_BOT_TOKEN = "7050933698:AAGfPMgQq_i3SGsQwmEqgTs7RwlWAptkjPE"; //TODO: добавь токен бота в кавычках
    public static final String OPEN_AI_TOKEN = "gpt:FUj5RRFaxyJPx8r99jGlJFkblB3TiwFfStVTSk3zPf89Bss9"; //TODO: добавь токен ChatGPT в кавычках

    private ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode currentMode = null;


    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }


    @Override
    public void onUpdateEventReceived(Update update) {
        //TODO: основной функционал бота будем писать здесь
        String messageUser = getMessageText();
        if (messageUser.equals("/start")){
            currentMode = DialogMode.MAIN;
            sendPhotoMessage("main");
            String textForUser = loadMessage("main");
            sendTextMessage(textForUser);

            showMainMenu("главное меню бота", "/start",
            "генерация Tinder-профля \uD83D\uDE0E", "/profile",
            "сообщение для знакомства \uD83E\uDD70", "/opener",
            " переписка от вашего имени \uD83D\uDE08", "/message",
            "переписка со звездами \uD83D\uDD25", "/date",
            "задать вопрос чату GPT \uD83E\uDDE0", "/gpt");
            return;
        }

        if (messageUser.equals("/gpt")){
            currentMode = DialogMode.GPT;
            sendPhotoMessage("gpt");
            String text = loadMessage("gpt");
            sendTextMessage(text);
            return;
        }
        if (currentMode == DialogMode.GPT){
            String prompt = loadPrompt("gpt");
           String answer = chatGPT.sendMessage(prompt, messageUser);
           sendTextMessage(answer);
            return;
        }
        sendTextMessage("*Привет!*");
        sendTextMessage("_Привет!_");

        sendTextMessage("Вы написали " + messageUser);
        sendTextButtonsMessage("Выберите режим работы:",
                "старт", "START",
                          "стоп", "STOP");

    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
