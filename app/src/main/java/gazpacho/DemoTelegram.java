package gazpacho;

import gazpacho.core.ingress.telegram.IngressBot;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
public class DemoTelegram {

    private static final String TOKEN = "7772111263:AAGRoo-Zf1meEP1Rw2fTuHa6E-XYqH9pO7Y";

    static {
        BasicConfigurator.configure();
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new IngressBot(TOKEN));
    }
}
