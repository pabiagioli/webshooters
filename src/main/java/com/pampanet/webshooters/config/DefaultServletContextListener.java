package com.pampanet.webshooters.config;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.pampanet.webshooters.modules.rest.BootstrapRestPackagesModule;
import com.pampanet.webshooters.modules.shiro.DefaultShiroModule;
import org.apache.shiro.guice.aop.ShiroAopModule;
import org.jboss.resteasy.plugins.guice.ext.RequestScopeModule;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.Arrays;
import java.util.List;

/**
 * Default implementation with DefaultShiroModule.
 * Extend this listener with @WebListener(value="guiceListener")
 *
 * @author pampa
 */
public class DefaultServletContextListener extends GenericGuiceRestEasyContextListener {

    private static final XLogger logger = XLoggerFactory.getXLogger(DefaultServletContextListener.class);

    @Override
    protected List<? extends Module> getModules(ServletContext context) {
        logger.entry(context);
        return Arrays.asList(new BootstrapPropertiesModule(), new RequestScopeModule(), new DefaultShiroModule(context), new ShiroAopModule(), new BootstrapRestPackagesModule());
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.entry(event);
        context = event.getServletContext();
        super.contextInitialized(event);
        logger.exit(event);
    }

    @Override
    protected void withInjector(Injector injector) {
        logger.entry(injector);
        super.withInjector(injector);
        context.setAttribute(INJECTOR_NAME, injector);
        logger.exit(injector);
    }
}
