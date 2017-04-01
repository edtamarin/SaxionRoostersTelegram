import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by Egor Tamarin on 29-Mar-17.
 * Main class.
 */
public class MainProcessing {
    public static void main(String args[]){
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new RoostersBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
