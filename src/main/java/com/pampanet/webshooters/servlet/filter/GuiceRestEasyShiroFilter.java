package com.pampanet.webshooters.servlet.filter;

import com.google.inject.Injector;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Extend this ShiroFilter with Servlet 3.x annotations like the following:<br><br>
 *
 * '@WebFilter(asyncSupported=true, value="/*", filterName="shiroFilter", initParams=@WebInitParam(name="staticSecurityManagerEnabled", value="true"))'<br>
 * public class myShiroFilter extends GuiceRestEasyShiroFilter{...}
 * <br>
 * <p>For Shiro Annotations to work properly you need to place this filter in the first position of the filterChain via web.xml</p>
 *
 * @author pampa
 */
public class GuiceRestEasyShiroFilter extends AbstractShiroFilter {
    private final XLogger logger = XLoggerFactory.getXLogger(GuiceRestEasyShiroFilter.class);

    private Injector injector;

    @Override
    public void init() throws Exception {
        logger.entry();
        super.init();
        injector = (Injector) getServletContext().getAttribute(Injector.class.getName());
        this.setSecurityManager(injector.getInstance(WebSecurityManager.class));
        this.setFilterChainResolver(injector.getInstance(FilterChainResolver.class));
        logger.exit();
    }

    public GuiceRestEasyShiroFilter() {
        logger.entry();
        logger.exit();
    }

}
