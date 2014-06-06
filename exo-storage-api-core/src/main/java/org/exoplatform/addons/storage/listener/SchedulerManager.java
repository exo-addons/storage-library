package org.exoplatform.addons.storage.listener;

import org.exoplatform.addons.storage.api.IScheduler;
import org.exoplatform.addons.storage.api.SchedulerFactory;
import org.quartz.SchedulerException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class SchedulerManager implements ServletContextListener {

    private static IScheduler schedulerService;
    private static SchedulerFactory schedulerFactory;
    private static Logger log = Logger.getLogger("SchedulerManager");

    public SchedulerManager() {

        schedulerFactory = new SchedulerFactory();

    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        log.info("INITIALIZING SCHEDULER");

        //--- Added with storage-api to ensure that SchedulerService is executed once
        if ( schedulerService == null) {

            schedulerService = schedulerFactory.createScheduler();

        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        log.info("CLOSING SCHEDULER");

        try {

            schedulerService.shutdown();

        } catch (SchedulerException e) {

            log.warning("for some reasons, Scheduler didn't want to stop");
        }
    }

}