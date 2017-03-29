import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * Created by Egor Tamarin on 29-Mar-17.
 */
public class RoostersBot extends TelegramLongPollingBot{
    @Override
    public void onUpdateReceived(Update update) {
        // TODO
    }

    @Override
    public String getBotUsername() {
        return "roosters_bot";
    }

    @Override
    public String getBotToken() {
        return "";
        return null;
    }
}
