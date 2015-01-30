package com.pampanet.webshooters.rest;

import com.google.inject.Inject;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rest/sample")
public class WebServiceTest {

    @Context
    ServletContext ctx;

    @Inject
    public WebServiceTest() {
    }

    @GET
    public Response sample() {
        return Response.ok().entity("helloWorld").type(MediaType.APPLICATION_JSON_TYPE).build();
    }

}
