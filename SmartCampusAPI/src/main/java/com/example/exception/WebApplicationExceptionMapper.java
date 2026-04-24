/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.exception;

import com.example.model.ErrorMessage;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Sevin Kawsika
 */

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {

        // Get the specific error code (like 405 or 404)
        int status = exception.getResponse().getStatus();

        // Convert it to our clean JSON format
        ErrorMessage error = new ErrorMessage(
                "Web Error: Method Not Allowed or URL Not Found",
                status
        );

        return Response.status(status)
                .entity(error)
                .build();
    }
}
