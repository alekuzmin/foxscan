package ru.foxscan.servlet;

import ru.foxscan.base.Runner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InitServlet implements ServletContextListener {

    public void contextInitialized(ServletContextEvent e) {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(new Runner(), 0, 10, TimeUnit.SECONDS);
    }

}
