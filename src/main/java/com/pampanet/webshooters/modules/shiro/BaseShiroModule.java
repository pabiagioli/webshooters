package com.pampanet.webshooters.modules.shiro;

import org.apache.shiro.config.Ini;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import javax.servlet.ServletContext;

/**
 * This Module loads a Sample IniRealm. This is also a Guice's PrivateModule.<br>
 * For injecting any of the providers listed here, you need to expose them through configureShiroWeb() method.<br>
 *
 * @author pampa
 */
public class BaseShiroModule extends ShiroWebModule {

    private static final String CREDENTIALS_MATCHER_ALGORITHM_NAME = "SHA-512";
    protected final XLogger logger = XLoggerFactory.getXLogger(getClass());

    public BaseShiroModule(ServletContext servletContext) {
        super(servletContext);
        logger.entry(servletContext);
        logger.exit(servletContext);
    }

    @Override
    protected void configureShiroWeb() {
        logger.entry();
        //if you would like to expose the CredentialsMatcher listed here, uncomment the following line.
        //expose(CredentialsMatcher.class);
        expose(WebSecurityManager.class);
        expose(FilterChainResolver.class);

        //avoid 4 times instantiation
        //bindRealm().to(IniRealm.class);

		/*addFilterChain("/logout", LOGOUT);
		addFilterChain("/rest/**",NO_SESSION_CREATION, AUTHC_BASIC);
		addFilterChain("/**", AUTHC_BASIC);*/
        logger.exit();
    }

    //@Provides
    //@Singleton
    IniRealm provideDefaultIniRealm(Ini ini) {
        logger.entry();
        IniRealm result = new IniRealm(ini);
        logger.exit(result);
        return result;
    }

    //@Provides
    //@Singleton
    Ini loadDefaultShiroIni() {
        logger.entry();
        Ini result = Ini.fromResourcePath("classpath:shiro.ini");
        logger.exit(result);
        return result;
    }

}
