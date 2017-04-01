import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;

/**
 * Created by Egor Tamarin on 29-Mar-17.
 * Handles the actions of the bot.
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
                                "search: \n/search <ID or name>\n\n" +
                                "inform you of the schedule: \n/schedule group/teacher <ID> <DAY(optional)>\n\n" +
                                "tell you about me: \n/about\n" +
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
                // User sends /search
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
                if (textOut.equals("")){textOut = "Whoops. Can't search for that.";}
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText(textOut);
                try {
                    sendMessage(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message_text.contains("/schedule")) {
                // User sends /schedule
                String[] split = message_text.split(" ");
                ArrayList<String> response = new ArrayList<>();
                String textOut = "";
                if ((split.length >= 3)&&(split.length <= 4)){
                    if (split[1].equals("group")){ // check what we are looking for
                        try { // check different cases since no optional params in Java
                            if (split.length == 3){response = botAPI.schedule(split[2],"group","");}
                            if (split.length == 4){response = botAPI.schedule(split[2],"group",split[3]);}
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        for (String data:response){
                            textOut += data;
                            textOut += "\n";
                        }
                    }else if (split[1].equals("teacher")){
                        try { // same as above
                            if (split.length == 3){response = botAPI.schedule(split[2],"teacher","");}
                            if (split.length == 4){response = botAPI.schedule(split[2],"teacher",split[3]);}
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        for (String data:response){
                            textOut += data;
                            textOut += "\n";
                        }
                    }else{
                        textOut = "I don't understand what do you want to search for...";
                    }
                }else{
                    textOut = "It seems your syntax is incorrect. /start for advice!";
                }
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText(textOut);
                try {
                    sendMessage(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else{
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("I can't recognize the command. Could you double-check that for me?");
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
