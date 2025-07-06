package gazpacho.core.ingress.telegram;

import gazpacho.DemoJdbc;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@RequiredArgsConstructor
public class IngressBot extends TelegramLongPollingBot {
    private static final String USERNAME = "PlexosBot";
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoJdbc.class);

    @NonNull
    private final String token;

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        User user = msg.getFrom();
        LOGGER.info("Update {} received from {}", msg, user);
    }
}
