/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.resource;

import com.example.dao.MockDatabase;
import com.example.exception.DataNotFoundException;
import com.example.exception.LinkedResourceNotFoundException;
import com.example.exception.SensorUnavailableException;
import com.example.model.ErrorMessage;
import com.example.model.Sensor;
import com.example.model.SensorReading;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Sevin Kawsika
 */

@Path("/readings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    @GET
    @Path("/sensor/{sensorId}")
    public List<SensorReading> getReadingsForSensor(@PathParam("sensorId") String sensorId) {
        if (!MockDatabase.SENSORS.containsKey(sensorId)) {
            throw new DataNotFoundException("Sensor with ID " + sensorId + " not found.");
        }

        return MockDatabase.READINGS.getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    public Response addReading(SensorReading reading, @Context UriInfo uriInfo) {
        if (reading == null || reading.getId() == null || reading.getSensorId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Reading ID and sensorId are required.", 400))
                    .build();
        }

        Sensor sensor = MockDatabase.SENSORS.get(reading.getSensorId());

        // 1. Dependency Validation
        if (sensor == null) {
            throw new LinkedResourceNotFoundException(
                    "Cannot add reading. Sensor with ID '" + reading.getSensorId() + "' does not exist."
            );
        }

        // 2. State Validation
        if (!"ACTIVE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Cannot accept reading. Sensor '" + sensor.getId() + "' is currently " + sensor.getStatus()
            );
        }

        // Set timestamp if not provided by client
        if (reading.getTimestamp() == null) {
            reading.setTimestamp(new Date());
        }

        // Save reading
        MockDatabase.READINGS.putIfAbsent(reading.getSensorId(), new ArrayList<>());
        MockDatabase.READINGS.get(reading.getSensorId()).add(reading);

        // 3. Update the Sensor's current value dynamically
        sensor.setCurrentValue(reading.getValue());

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(reading.getId())
                .build();

        return Response.created(location)
                .entity(reading)
                .build();
    }
}
