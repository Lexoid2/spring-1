package com.javarush.kostenko;

import com.javarush.kostenko.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import jakarta.servlet.Servlet;

/**
 * Entry point of the application.
 * This class initializes and starts an embedded Tomcat server with a Spring ApplicationContext.
 */
@Slf4j
public class Application {

    /**
     * Main method to start the embedded Tomcat server and configure the Spring ApplicationContext.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            log.info("Starting embedded Tomcat server...");

            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8080);
            tomcat.getConnector();

            AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
            context.register(AppConfig.class);
            log.info("Spring ApplicationContext initialized with AppConfig.");

            Servlet dispatcherServlet = new DispatcherServlet(context);

            Context appContext = tomcat.addContext("", null);
            Tomcat.addServlet(appContext, "dispatcher", dispatcherServlet).setLoadOnStartup(1);
            appContext.addServletMappingDecoded("/", "dispatcher");

            tomcat.start();
            log.info("Embedded Tomcat server started on port 8080.");

            tomcat.getServer().await();
        } catch (Exception e) {
            log.error("An error occurred while starting the embedded Tomcat server.", e);
        }
    }
}
