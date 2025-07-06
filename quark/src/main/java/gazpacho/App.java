package gazpacho;

import gazpacho.web.controller.DownloadController;
import jakarta.inject.Inject;
import org.apache.log4j.BasicConfigurator;

public class App {

    @Inject
    DownloadController downloadController;

    public static void main(String[] args) {
        BasicConfigurator.configure();
    }
}
