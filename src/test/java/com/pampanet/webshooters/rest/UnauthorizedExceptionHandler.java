package com.pampanet.webshooters.rest;

import org.apache.shiro.authz.UnauthorizedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by pampa on 31/01/15.
 */
@Provider
public class UnauthorizedExceptionHandler implements ExceptionMapper<UnauthorizedException> {
    @Override
    public Response toResponse(UnauthorizedException exception) {
        return Response.status(401).entity(exception.getMessage()).build();
    }
}
