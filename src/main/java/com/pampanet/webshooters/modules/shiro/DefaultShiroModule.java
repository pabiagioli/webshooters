package com.pampanet.webshooters.modules.shiro;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.shiro.config.Ini;
import org.apache.shiro.realm.text.IniRealm;

import javax.servlet.ServletContext;

public class DefaultShiroModule extends BaseShiroModule {

    public DefaultShiroModule(ServletContext servletContext) {
        super(servletContext);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void configureShiroWeb() {
        super.configureShiroWeb();

        bindRealm().to(IniRealm.class);

        addFilterChain("/logout", LOGOUT);
        addFilterChain("/rest/**", NO_SESSION_CREATION, AUTHC_BASIC);
        addFilterChain("/**", AUTHC_BASIC);
    }

    @Provides
    @Singleton
    IniRealm provideIniRealm(Ini ini) {
        return super.provideDefaultIniRealm(ini);
    }

    @Provides
    @Singleton
    Ini loadShiroIni() {
        return super.loadDefaultShiroIni();
    }
}
