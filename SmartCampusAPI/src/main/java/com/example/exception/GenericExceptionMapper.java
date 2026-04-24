/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.exception;

import com.example.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Sevin Kawsika
 */

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof javax.ws.rs.WebApplicationException) {
            return ((javax.ws.rs.WebApplicationException) exception).getResponse();
        }

        // This remains for actual code crashes (real 500 errors)
        ErrorMessage error = new ErrorMessage(
                "An unexpected internal server error occurred.",
                500
        );

        // This is important for debugging: print the error to the server console
        exception.printStackTrace();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }
}
