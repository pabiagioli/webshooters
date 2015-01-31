package com.pampanet.webshooters.modules.rest;

import com.google.inject.AbstractModule;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * This Module binds all the classes under the declared packages
 *
 * @author pampa
 */
public class BootstrapRestPackagesModule extends AbstractModule {

    private static final XLogger logger = XLoggerFactory.getXLogger(BootstrapRestPackagesModule.class);

    private static final String DEFAULT_SCAN_REST_PACKAGES_PROPERTY = "com.pampanet.webshooters.config.rest.pkgs";

    private static final String DEFAULT_REST_PACKAGE_TO_SCAN = "com.pampanet.webshooters.rest";

    private static final String DEFAULT_REST_CONFIG_PROPERTIES_FILE = "/webshooters-rest-config.properties";

    private static final String COMMA = ",";

    private String packagesToScanKey = DEFAULT_SCAN_REST_PACKAGES_PROPERTY;
    private String propertiesFileName = DEFAULT_REST_CONFIG_PROPERTIES_FILE;

    public BootstrapRestPackagesModule(){
        this(DEFAULT_REST_CONFIG_PROPERTIES_FILE,DEFAULT_SCAN_REST_PACKAGES_PROPERTY);
    }

    /**
     * Constructor with given properties file name and given key to retrieve
     * @param properties file name
     * @param pkg_key key to retrieve in properties file
     */
    public BootstrapRestPackagesModule(String properties, String pkg_key){
        propertiesFileName = properties;
        packagesToScanKey = pkg_key;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void configure() {
        logger.entry();

        String[] pkgs = getWebShootersConfigProperties().getProperty(
                getPackagesToScanKey(), DEFAULT_REST_PACKAGE_TO_SCAN)
                .split(COMMA);

        for (String pkg : pkgs) {
            logger.info("found RESTful package: {}", pkg.trim());
            Class[] lst = null;
            try {
                lst = getClasses(pkg.trim());
            } catch (ClassNotFoundException | IOException e) {
                logger.error("{}, {}", e.getClass().getName(), e.getMessage());
                e.printStackTrace();
            }
            assert lst != null;
            for (Class c : lst) {
                if (c.isAnnotationPresent(Path.class)
                        || c.isAnnotationPresent(Provider.class)) {
                    logger.info("found JAX-RS Resource: {}", c.getName());
                    bind(c);
                }
            }
        }
        logger.exit();
    }

    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @SuppressWarnings({"rawtypes"})
    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        Class[] result;// = null;
        logger.entry();
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        result = classes.toArray(new Class[classes.size()]);
        logger.exit(result);
        return result;
    }

    /**
     * Recursive method used to find all classes in a given directory and
     * subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("rawtypes")
    private static List<Class> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        logger.entry();
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file,
                        packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName
                        + '.'
                        + file.getName().substring(0,
                        file.getName().length() - 6)));
            }
        }
        logger.exit(classes);
        return classes;
    }

    private Properties getWebShootersConfigProperties() {
        Properties bootstrapProperties = new Properties();

        try {
            InputStream is = getClass().getResourceAsStream(getPropertiesFileName());
            bootstrapProperties.load(is);
        } catch (Exception e) {
            logger.throwing(e);
        }

        return bootstrapProperties;
    }

    public String getPackagesToScanKey() {
        return packagesToScanKey;
    }

    public String getPropertiesFileName() {
        return propertiesFileName;
    }
}
