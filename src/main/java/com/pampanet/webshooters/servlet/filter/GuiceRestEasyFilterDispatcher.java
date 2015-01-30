package com.pampanet.webshooters.servlet.filter;

import com.google.inject.Binding;
import com.google.inject.Injector;
import org.jboss.resteasy.plugins.guice.GuiceResourceFactory;
import org.jboss.resteasy.plugins.server.servlet.Filter30Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.FilterBootstrap;
import org.jboss.resteasy.plugins.server.servlet.ServletContainerDispatcher;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResourceFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.util.GetRestful;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import javax.inject.Inject;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

/**
 * Extend this RestEasy custom class to add an async web filter to the app.<br>
 * i.e.:<br> @WebFilter(asyncSupported=true, value="/*", filterName="myFilter")<br>
 * public class MyCustomAsyncFilter extends GuiceRestEasyFilterDispatcher{...}
 *
 * @author pampa
 */
public class GuiceRestEasyFilterDispatcher extends Filter30Dispatcher {

    private static final XLogger logger = XLoggerFactory.getXLogger(GuiceRestEasyFilterDispatcher.class);

    @Inject
    Injector injector;

    public GuiceRestEasyFilterDispatcher() {
        logger.entry();
        logger.exit();
    }

    @Override
    public void init(FilterConfig servletConfig) throws ServletException {
        logger.entry();
        servletContainerDispatcher = new ServletContainerDispatcher();
        FilterBootstrap bootstrap = new FilterBootstrap(servletConfig);
        servletContext = servletConfig.getServletContext();
        servletContainerDispatcher.init(servletContext, bootstrap, this, this);
        servletContainerDispatcher.getDispatcher().getDefaultContextObjects().put(FilterConfig.class, servletConfig);

        if (injector == null)
            injector = (Injector) servletConfig.getServletContext().getAttribute(Injector.class.getName());

        Registry registry = getDispatcher().getRegistry();
        ResteasyProviderFactory providerFactory = getDispatcher().getProviderFactory();

        for (final Binding<?> binding : injector.getBindings().values()) {
            Type type = binding.getKey().getTypeLiteral().getType();
            if (type instanceof Class) {
                Class<?> beanClass = (Class<?>) type;
                if (GetRestful.isRootResource(beanClass)) {
                    ResourceFactory resourceFactory = new GuiceResourceFactory(binding.getProvider(), beanClass);
                    logger.info("registering factory for {}", beanClass.getName());
                    registry.addResourceFactory(resourceFactory);
                }

                if (beanClass.isAnnotationPresent(Provider.class)) {
                    logger.info("registering provider instance for {}", beanClass.getName());
                    providerFactory.registerProviderInstance(binding.getProvider().get());
                }
            }
        }
        logger.exit();
    }

}