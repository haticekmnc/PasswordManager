package log;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

@Singleton
@Startup
public class ApplicationStartup {

    private static final Logger LOGGER = Logger.getLogger(ApplicationStartup.class.getName());

    @PostConstruct
    public void init() {
        setupLogging();
        LOGGER.fine("Logging is configured.");
    }

    public static void setupLogging() {
        StreamHandler sh = new StreamHandler(System.out, new SimpleFormatter());
        sh.setLevel(Level.FINE);

        LOGGER.addHandler(sh);
        LOGGER.setLevel(Level.FINE);
        LOGGER.setUseParentHandlers(false);
    }
}
