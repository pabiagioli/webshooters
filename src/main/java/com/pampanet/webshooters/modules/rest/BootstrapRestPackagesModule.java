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

    protected static final String SCAN_REST_PACKAGES_PROPERTY = "com.pampanet.mate.config.rest.pkgs";

    protected static final String DEFAULT_REST_PACKAGE_TO_SCAN = "com.pampanet.mate.rest";

    protected static final String MATE_REST_CONFIG_PROPERTIES_FILE = "/webshooters-rest-config.properties";


    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void configure() {
        logger.entry();

        String[] pkgs = getMateConfigProperties().getProperty(
                SCAN_REST_PACKAGES_PROPERTY, DEFAULT_REST_PACKAGE_TO_SCAN)
                .split(",");

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

    private Properties getMateConfigProperties() {
        Properties bootstrapProperties = new Properties();

        try {
            InputStream is = getClass().getResourceAsStream(MATE_REST_CONFIG_PROPERTIES_FILE);
            bootstrapProperties.load(is);
        } catch (Exception e) {
            logger.throwing(e);
        }

        return bootstrapProperties;
    }

}
