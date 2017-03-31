import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;

/**
 * Created by Egor Tamarin on 29-Mar-17.
 */
public class RoostersBot extends TelegramLongPollingBot{
    @Override
    public void onUpdateReceived(Update update) {
        long chat_id = 0;
        String message_text;
        RoosterAPI botAPI = new RoosterAPI();
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            message_text = update.getMessage().getText();
            chat_id = update.getMessage().getChatId();
            if (message_text.equals("/start")) {
                // User send /start
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Welcome to the Saxion Roosters bot.\n" +
                                "I can:\n" +
                                "search: /search <ID or name>\n" +
                                "inform you of the schedule: /schedule <group>\n" +
                                "tell you about me: /about\n" +
                                "\n" +
                                "I am updated constantly, albeit slowly.");
                try {
                    sendMessage(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if(message_text.equals("/about")){
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("I am the Saxion Roosters bot.\n" +
                                "\n" +
                                "My creator:\n" +
                                "Egor Tamarin, 2017\n" +
                                "Send feedback or suggestions to:\n" +
                                "Saxion mail or\n" +
                                "edtfeedback@gmail.com\n" +
                                "\n" +
                                "Have a nice day!");
                try {
                    sendMessage(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message_text.contains("/search")) {
                // User send /start
                String[] split = message_text.split(" ");
                ArrayList<String> response = new ArrayList<>();
                try {
                    response = botAPI.search(split[1]);
                }catch(Exception e){
                    e.printStackTrace();
                }
                String textOut = "";
                for (String data:response){
                    textOut += data;
                    textOut += "\n";
                }
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText(textOut);
                try {
                    sendMessage(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message_text.contains("/schedule")) {
                // User sends/schedule
                String[] split = message_text.split(" ");
                ArrayList<String> response = new ArrayList<>();
                try {
                    response = botAPI.schedule(split[1]);
                }catch(Exception e){
                    e.printStackTrace();
                }
                String textOut = "";
                for (String data:response){
                    textOut += data;
                    textOut += "\n";
                }
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText(textOut);
                try {
                    sendMessage(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "roosters_bot";
    }

    @Override
    public String getBotToken() {
        return "359417360:AAFmNBdFViyTnpBrV2aaMaBKnp8Vc4vXi-M";
    }
}
