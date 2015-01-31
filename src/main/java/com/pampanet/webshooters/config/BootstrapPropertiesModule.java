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

    private static final XLogger logger = XLoggerFactory.getXLogger(BootstrapPropertiesModule.class);
    private static final String DEFAULT_BOOTSTRAP_PROPERTIES_FILE = "webshooters-config.properties";
    private static final String SLASH = System.getProperty("file.separator");

    private String bootstrapPropertiesFile = DEFAULT_BOOTSTRAP_PROPERTIES_FILE;

    /**
     * Default no-arg constructor will use default values
     */
    public BootstrapPropertiesModule(){
        this(DEFAULT_BOOTSTRAP_PROPERTIES_FILE);
    }

    /**
     * Constructor with custom properties file
     * @param properties file name
     */
    public BootstrapPropertiesModule(String properties){
        assert properties != null;
        this.bootstrapPropertiesFile = properties;
    }

    @Override
    protected void configure() {
        logger.entry();
        Properties bootstrapProperties = new Properties();
        try {
            InputStream is = getClass().getResourceAsStream(SLASH + getBootstrapPropertiesFile());
            bootstrapProperties.load(is);
            Names.bindProperties(binder(), bootstrapProperties);
        } catch (IOException e) {
            logger.error("I/O Exception during configuration loading");
            logger.throwing(e);
        }
        logger.exit();
    }

    public String getBootstrapPropertiesFile() {
        return bootstrapPropertiesFile;
    }
}
