package gazpacho.core.download.filelist.navigate;

import lombok.NonNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

import java.io.IOException;

public class SessionHandler {
    private static final String URL = "https://filelist.io";
    private static final String INCORRECT_LOGIN_REL_URL = "takelogin.php";
    private static final String URL_PATH_SEP = "/";
    private static final String INCORRECT_LOGIN_URL = URL + URL_PATH_SEP + INCORRECT_LOGIN_REL_URL;
    private static final String INPUT_FORM_SELECTOR = "[method=post]";
    private static final String USERNAME_FIELD_SELECTOR = "#username";
    private static final String PASSWORD_FIELD_SELECTOR = "#password";

    private final String username;
    private final String password;
    private final int timeoutS;
    private final int maxBodySizeMb;

    public SessionHandler(@NonNull String username,
                          @NonNull String password,
                          int timeoutS,
                          int maxBodySizeMb) {
        this.username = username;
        this.password = password;
        this.timeoutS = timeoutS;
        this.maxBodySizeMb = maxBodySizeMb;
    }

    public Connection getActiveSession() throws IOException {
        Connection connection = Jsoup.newSession()
                .timeout(timeoutS * 1000)
                .maxBodySize(maxBodySizeMb * 1024 * 1024)
                .ignoreContentType(true)
                .url(URL);

        login(connection);
        return connection;
    }

    private void login(Connection connection) throws IOException {
        Document landingPage = connection.newRequest().get();
        FormElement form = (FormElement) landingPage.selectFirst(INPUT_FORM_SELECTOR);
        Element usernameField = form.selectFirst(USERNAME_FIELD_SELECTOR);
        usernameField.val(this.username);
        Element passwordField = form.selectFirst(PASSWORD_FIELD_SELECTOR);
        passwordField.val(this.password);
        Connection.Response mainPage = form.submit().ignoreContentType(true).execute();

        Document mainPageDoc = mainPage.parse();
        if (INCORRECT_LOGIN_URL.equals(mainPageDoc.location())) {
            throw new IllegalStateException("Login Failed. Invalid password or username.");
        }
    }
}
