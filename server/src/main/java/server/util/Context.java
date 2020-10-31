package server.util;

import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Context implements ServletContextListener {

    /** This method is called when the context is first started */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // We init config in order to read the file and set all the variables.
        try {
            Config.initializeConfig();
        } catch (IOException e) {
            System.out.println("Can't read config");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Context is closed");
    }
}
