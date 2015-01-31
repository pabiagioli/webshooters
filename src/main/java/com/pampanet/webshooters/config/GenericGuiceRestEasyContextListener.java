package com.pampanet.webshooters.config;

import com.google.inject.Injector;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * Default implementation with DefaultShiroModule.
 * Extend this listener with @WebListener(value="guiceListener")
 *
 * @author pampa
 *
 */
public class GenericGuiceRestEasyContextListener extends GuiceResteasyBootstrapServletContextListener {

    protected static final String INJECTOR_NAME = Injector.class.getName();
    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        servletContext = event.getServletContext();
        super.contextInitialized(event);
    }

    public ServletContext getServletContext() {
        return servletContext;
    }
}
