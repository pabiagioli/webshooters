package com.pampanet.webshooters.config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Guice Module to load Properties file and bind it to Guice Injector.<br>
 * Properties can later be used in constructor or field injection by using: <br>
 * <code>@Inject @Named("name.of.the.key") private String propValue;</code>
 *
 * @author pampa
 */
public class BootstrapPropertiesModule extends AbstractModule {

    private XLogger logger = XLoggerFactory.getXLogger(this.getClass());

    protected static String BOOTSTRAP_PROPERTIES_FILE = "webshooters-config.properties";
    private static final String SLASH = System.getProperty("file.separator");

    @Override
    protected void configure() {
        logger.entry();
        Properties bootstrapProperties = new Properties();
        try {
            InputStream is = getClass().getResourceAsStream(SLASH + BOOTSTRAP_PROPERTIES_FILE);
            bootstrapProperties.load(is);
            Names.bindProperties(binder(), bootstrapProperties);
        } catch (FileNotFoundException e) {
            logger.error("The configuration file " + BOOTSTRAP_PROPERTIES_FILE + " can not be found");
            logger.throwing(e);
        } catch (IOException e) {
            logger.error("I/O Exception during loading configuration");
            logger.throwing(e);
        }
        logger.exit();
    }

}
