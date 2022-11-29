import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.Random;


public class Bot extends TelegramLongPollingBot {

    private InlineKeyboardMarkup keyboardMarkup;

    private InlineKeyboardButton aa = InlineKeyboardButton.builder().text(" ").callbackData("setAA").build();
    private InlineKeyboardButton ab = InlineKeyboardButton.builder().text(" ").callbackData("setAB").build();
    private InlineKeyboardButton ac = InlineKeyboardButton.builder().text(" ").callbackData("setAC").build();
    private InlineKeyboardButton ba = InlineKeyboardButton.builder().text(" ").callbackData("setBA").build();
    private InlineKeyboardButton bb = InlineKeyboardButton.builder().text(" ").callbackData("setBB").build();
    private InlineKeyboardButton bc = InlineKeyboardButton.builder().text(" ").callbackData("setBC").build();
    private InlineKeyboardButton ca = InlineKeyboardButton.builder().text(" ").callbackData("setCA").build();
    private InlineKeyboardButton cb = InlineKeyboardButton.builder().text(" ").callbackData("setCB").build();
    private InlineKeyboardButton cc = InlineKeyboardButton.builder().text(" ").callbackData("setCC").build();

    private InlineKeyboardButton start = InlineKeyboardButton.builder().text("Start").callbackData("/start").build();
    private InlineKeyboardButton join = InlineKeyboardButton.builder().text("Join").callbackData("/join").build();
    private InlineKeyboardButton clear = InlineKeyboardButton.builder().text("Clear").callbackData("/clear").build();

    private User playerOne;
    private User playerTwo;
    private boolean gameIsStarted;
    private boolean gameProcessing;
    private int gameStatus;
    private int inTurn = 0;

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot();
        botsApi.registerBot(bot);
    }

    @Override
    public String getBotUsername() {
        return Token.getBotName();
    }

    @Override
    public String getBotToken() {
        return Token.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().getText() != null) {
                String message = update.getMessage().getText();
                User user = update.getMessage().getFrom();
                Long chatId = update.getMessage().getChatId();

                if (message.equals("/start") && !gameIsStarted && !gameProcessing) {
                    String outMessage = user.getFirstName().toString() + " " + "играет " + (new Random().nextBoolean() ? "крестиками\n" : "ноликами\n")
                            + "Игра начнется после команды \n/join от второго игрока";
                    if (outMessage.contains("крестиками")) {
                        playerOne = user;
                        gameIsStarted = true;
                    }
                    if (outMessage.contains("ноликами")) {
                        playerTwo = user;
                        gameIsStarted = true;
                    }
                    sendText(chatId, outMessage);
                }
                if (message.equals("/join") && gameIsStarted && !gameProcessing) {
                    if ((playerOne != null && (long) playerOne.getId() != (long) user.getId()) ||
                            (playerTwo != null && (long) playerTwo.getId() != (long) user.getId())) {
                        String outMessage = user.getFirstName().toString() + " " + "играет " + (playerOne == null ? "крестиками\n" : "ноликами\n")
                                + "Игра начинается!\nПервыми ходят крестики.";
                        if (outMessage.contains("крестиками")) {
                            playerOne = user;
                            gameProcessing = true;
                        }
                        if (outMessage.contains("ноликами")) {
                            playerTwo = user;
                            gameProcessing = true;
                        }
                        sendText(chatId, outMessage);
                    }
                }
                if (message.equals("/clear")) {
                    gameToDefault();
                    String outMessage = "Состояние игры обнулилось.\nЧто бы сыграть еще раз\nвведите команду /start";
                    sendText(chatId, outMessage);
                }
            }
        }
//Если нажата кнопка проверяем, что игрок, нажавший ее, сделал это в свою очередь хода, проверяем какую именно нажал
//=====================================================================================================================================
        if (update.hasCallbackQuery()) {
            System.out.println("f");
            String callBackQueryData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            User user = update.getCallbackQuery().getFrom();
            long messageButtonId = update.getCallbackQuery().getMessage().getMessageId();
            System.out.println("f");
            if (callBackQueryData.equals("/start")) {
                editText(chatId, messageButtonId);
            }
            if (callBackQueryData.equals("/join")) {
                join.setText("НАЖАЛ");
            }
            if (callBackQueryData.equals("/clear")) {
                clear.setText("НАЖАЛ");
            }
//------------------------------------------------  Нажатие на кнопку  ------------------------------------------------
            if (gameIsStarted && gameProcessing &&
                    (((long) user.getId() == (long) playerOne.getId() && inTurn % 2 == 0) ||
                            (((long) user.getId() == (long) playerTwo.getId()) && (inTurn % 2 == 1))) &&
                    (callBackQueryData.equals("setAA") || callBackQueryData.equals("setAB") || callBackQueryData.equals("setAC") ||
                            callBackQueryData.equals("setBA") || callBackQueryData.equals("setBB") || callBackQueryData.equals("setBC") ||
                            callBackQueryData.equals("setCA") || callBackQueryData.equals("setCB") || callBackQueryData.equals("setCC"))) {
                //Переменная для определения очередности игры
                //отправляем координату нажатой кнопки, в ответ получаем статус игры. Если 0 - игра продоложается. 1 - игрок сделавший ход выиграл , если 2 - ничья
                if (callBackQueryData.equals("setAA") && aa.getText().equals(" ")) {pushTheButton(0, 0, chatId, user, aa);}
                if (callBackQueryData.equals("setAB") && ab.getText().equals(" ")) {pushTheButton(0, 1, chatId, user, ab);}
                if (callBackQueryData.equals("setAC") && ac.getText().equals(" ")) {pushTheButton(0, 2, chatId, user, ac);}
                if (callBackQueryData.equals("setBA") && ba.getText().equals(" ")) {pushTheButton(1, 0, chatId, user, ba);}
                if (callBackQueryData.equals("setBB") && bb.getText().equals(" ")) {pushTheButton(1, 1, chatId, user, bb);}
                if (callBackQueryData.equals("setBC") && bc.getText().equals(" ")) {pushTheButton(1, 2, chatId, user, bc);}
                if (callBackQueryData.equals("setCA") && ca.getText().equals(" ")) {pushTheButton(2, 0, chatId, user, ca);}
                if (callBackQueryData.equals("setCB") && cb.getText().equals(" ")) {pushTheButton(2, 1, chatId, user, cb);}
                if (callBackQueryData.equals("setCC") && cc.getText().equals(" ")) {pushTheButton(2, 2, chatId, user, cc);}
            }
        }
    }

    private void pushTheButton(int horizontal, int vertical, Long chatId, User user, InlineKeyboardButton button) {
        inTurn++;
        gameStatus = Game.makeAMove(horizontal, vertical, (inTurn % 2 == 1 ? 1 : 10));
        button.setText(inTurn % 2 == 1 ? "X" : "O");
        if (gameStatus == 2) {
            draw(chatId);
        } else if (gameStatus == 1) {
            winner(chatId, user);
        } else if (gameStatus == 0) {
            move(chatId, user);
        }
    }

    private void draw(Long chatId) {
        String outMessage = "Ничья!\nЧто бы сыграть еще раз\nвведите команду /start";
        sendText(chatId, outMessage);
        gameToDefault();
    }

    private void winner(Long chatId, User user) {
        String outMessage = user.getFirstName() + " выиграл!\nЧто бы сыграть еще раз\nвведите команду /start";
        sendText(chatId, outMessage);
        gameToDefault();
    }

    private void move(Long chatId, User user) {
        String outMessage = user.getFirstName() + " сделал ход";
        sendText(chatId, outMessage);
    }

    private void gameToDefault() {
        playerOne = null;
        playerTwo = null;
        gameIsStarted = false;
        gameProcessing = false;
        inTurn = 0;
        gameStatus = 0;
        Game.gameSpace = new int[3][3];
        aa.setText(" ");
        ab.setText(" ");
        ac.setText(" ");
        ba.setText(" ");
        bb.setText(" ");
        bc.setText(" ");
        ca.setText(" ");
        cb.setText(" ");
        cc.setText(" ");
    }

    public void sendText(Long chatId, String what) {
        keyboardMarkup = InlineKeyboardMarkup.builder().
                keyboardRow(List.of(aa, ab, ac)).
                keyboardRow(List.of(ba, bb, bc)).
                keyboardRow(List.of(ca, cb, cc)).
                keyboardRow(List.of(start)).
                keyboardRow(List.of(join)).
                keyboardRow(List.of(clear)).build();

        SendMessage sendMessage = SendMessage.builder().chatId(chatId.toString()).parseMode("HTML").text(what).replyMarkup(keyboardMarkup).build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("error");
            throw new RuntimeException(e);
        }
    }

    public void editText(Long chatId, long messageId) {
        keyboardMarkup = InlineKeyboardMarkup.builder().
                keyboardRow(List.of(aa, ab, ac)).
                keyboardRow(List.of(ba, bb, bc)).
                keyboardRow(List.of(ca, cb, cc)).
                keyboardRow(List.of(start)).
                keyboardRow(List.of(join)).
                keyboardRow(List.of(clear)).build();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId((int)messageId);
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setReplyMarkup(keyboardMarkup);
        editMessageText.setText("TEST");
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            System.out.println("error");
            throw new RuntimeException(e);
        }
    }

}