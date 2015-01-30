package com.pampanet.webshooters;

import com.pampanet.webshooters.config.DefaultServletContextListener;
import com.pampanet.webshooters.rest.WebServiceTest;
import com.pampanet.webshooters.servlet.filter.GuiceRestEasyFilterDispatcher;
import com.pampanet.webshooters.servlet.filter.GuiceRestEasyShiroFilter;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.shiro.codec.Base64;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Integration Test using Jetty embedded Server and OkHttp Client.<br>
 * This test uses WebServiceTest and Default implementations of ServletContextListener and ShiroModule.<br>
 * <p/>
 * <p>
 * Try modifying the config files under src/test/resources folder.
 * </p>
 *
 * @author pampa
 */
public class AppTest {

    private final String API_TEST_URL = "http://localhost:8080/rest/sample";
    private final String SAMPLE_USER_PASS_UNENCODED = "lonestarr:vespa";

    @Test
    public void runTestWebApp() throws Exception {
        Server server = new Server(8080);
        ServletContextHandler ctxHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ctxHandler.setContextPath("/");

        ctxHandler.addEventListener(new DefaultServletContextListener());
        ctxHandler.addFilter(GuiceRestEasyShiroFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        ctxHandler.addFilter(GuiceRestEasyFilterDispatcher.class, "/*", EnumSet.allOf(DispatcherType.class));

        server.setHandler(ctxHandler);
        // Start things up!
        server.start();

        WebServiceTest wsTest = new WebServiceTest();
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder().url(API_TEST_URL)
                .header("Authorization", "Basic " + Base64.encodeToString(SAMPLE_USER_PASS_UNENCODED.getBytes()))
                .get().build();

        String localResult = wsTest.sample().getEntity().toString();
        System.out.println("local= " + localResult);

        Response response = client.newCall(req).execute();
        String restFulResult = response.body().string();
        System.out.println("restFul= " + restFulResult);

        Assert.assertEquals(response.code(), 200);
        Assert.assertEquals(restFulResult, localResult);

        server.stop();
    }

}
