/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.exception;

import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Sevin Kawsika
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOG.info("---- Incoming Request ----");
        LOG.info("Method: " + requestContext.getMethod());
        LOG.info("Path: " + requestContext.getUriInfo().getPath());
        LOG.info("Headers: " + requestContext.getHeaders().keySet());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        LOG.info("---- Outgoing Response ----");
        LOG.info("Status: " + responseContext.getStatus() + " " + responseContext.getStatusInfo().getReasonPhrase());
        LOG.info("---------------------------");
    }
}
