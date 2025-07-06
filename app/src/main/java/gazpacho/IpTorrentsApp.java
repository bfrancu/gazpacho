package gazpacho;

import org.apache.log4j.BasicConfigurator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

public class IpTorrentsApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpTorrentsApp.class);
    private static final String URL = "https://iptorrents.com";
    private static final String USER = "umesman";
    private static final String PASS = "a=?aLEK.r6kZ9E.";
    private static final String INPUT_FORM_SELECTOR = "[method=post]";
    private static final String USERNAME_FIELD_SELECTOR = "[name=username]";
    private static final String PASSWORD_FIELD_SELECTOR = "[name=password]";

    static {
        BasicConfigurator.configure();
    }

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        connectWithSelenium();
    }

    private static void connectWithSelenium() throws URISyntaxException, MalformedURLException {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("javascript.enabled", "False");
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);
        options.addArguments("-headless");
        URL gridURL = new URI("http://localhost:4444").toURL();
        var driver = new RemoteWebDriver(gridURL, options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

//        driver.navigate().to("https://medium.com/@gangulysamapti11/web-scraping-using-selenium-java-and-jsoup-4274f92afa8f");
        driver.get(URL);
//        driver.findElement(By.cssSelector(""));
        LOGGER.info(driver.getPageSource());

        driver.quit();
    }

    private static void connectWithJsoup() throws IOException {
        Connection connection = Jsoup.newSession()
                .timeout(5 * 1000)
                .maxBodySize(50 * 1024 * 1024)
                .ignoreContentType(true)
                .url(URL);

        Map<String, String> headers = getConnectionHeaders();
        /*
         */
        connection.headers(headers);

        Document landingPage = connection.newRequest().get();
        FormElement form = (FormElement) landingPage.selectFirst(INPUT_FORM_SELECTOR);
        Element usernameField = form.selectFirst(USERNAME_FIELD_SELECTOR);
        usernameField.val(USER);
        Element passwordField = form.selectFirst(PASSWORD_FIELD_SELECTOR);
        passwordField.val(PASS);
        Connection.Response mainPage = form.submit().ignoreContentType(true).execute();

        LOGGER.info(mainPage.url().toString());
        Document mainPageDoc = mainPage.parse();
        LOGGER.info(mainPageDoc.toString());

    }

    private static Map<String, String> getConnectionHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, br, zstd");
        headers.put("Accept-Language", "en-US,en;q=0.5");
        headers.put("Connection", "keep-alive");
        headers.put("DNT", "1");
        headers.put("Host", "iptorrents.com");
        headers.put("Priority", "u=0, i");
        headers.put("Sec-Fetch-Dest", "document");
        headers.put("Sec-Fetch-Mode", "navigate");
        headers.put("Sec-Fetch-Site", "same-origin");
        headers.put("Sec-Fetch-User", "?!");
        headers.put("Sec-GPC", "1");
        headers.put("TE", "trailers");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:135.0) Gecko/20100101 Firefox/135.0");
//        headers.put("Origin", "https://iptorrents.com");
//        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }
}
