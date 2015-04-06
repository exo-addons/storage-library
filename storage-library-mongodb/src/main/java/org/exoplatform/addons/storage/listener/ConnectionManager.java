package org.exoplatform.addons.storage.listener;

import org.exoplatform.addons.storage.services.mongodb.MongoBootstrap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class ConnectionManager implements ServletContextListener {

    private static MongoBootstrap mongoBootstrap;
    private static Logger log = Logger.getLogger("ConnectionManager");

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("INITIALIZING MONGODB");
        mongoBootstrap = new MongoBootstrap();

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("CLOSING MONGODB");
        mongoBootstrap.close();
    }

    public static MongoBootstrap getInstance() {
         return mongoBootstrap;
    }

    public static MongoBootstrap forceNew() {

        log.warning("ConnectionManager.forceNew has been used : this should never happen in Production!");
        if (mongoBootstrap!=null) {
            mongoBootstrap.close();
        }

        mongoBootstrap = new MongoBootstrap();

        return mongoBootstrap;
  }
}
