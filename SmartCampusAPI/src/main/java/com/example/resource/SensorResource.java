/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.resource;

import com.example.dao.MockDatabase;
import com.example.exception.DataNotFoundException;
import com.example.exception.LinkedResourceNotFoundException;
import com.example.model.ErrorMessage;
import com.example.model.Room;
import com.example.model.Sensor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Sevin Kawsika
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public List<Sensor> getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<>(MockDatabase.SENSORS.values());

        // Optional filtering by type
        if (type != null && !type.trim().isEmpty()) {
            List<Sensor> filtered = new ArrayList<>();
            for (Sensor sensor : sensors) {
                if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type.trim())) {
                    filtered.add(sensor);
                }
            }
            return filtered;
        }

        return sensors;
    }

    @GET
    @Path("/{sensorId}")
    public Sensor getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = MockDatabase.SENSORS.get(sensorId);

        if (sensor == null) {
            throw new DataNotFoundException("Sensor with ID " + sensorId + " not found.");
        }

        return sensor;
    }

    @POST
    public Response addSensor(Sensor sensor, @Context UriInfo uriInfo) {
        if (sensor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Request body is required.", 400))
                    .build();
        }

        if (isBlank(sensor.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Sensor ID is required.", 400))
                    .build();
        }

        if (isBlank(sensor.getType())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Sensor type is required.", 400))
                    .build();
        }

        if (isBlank(sensor.getStatus())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Sensor status is required.", 400))
                    .build();
        }

        if (isBlank(sensor.getRoomId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("roomId is required.", 400))
                    .build();
        }

        if (MockDatabase.SENSORS.containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorMessage("Sensor with ID " + sensor.getId() + " already exists.", 409))
                    .build();
        }

        Room room = MockDatabase.ROOMS.get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException(
                    "Cannot create sensor. Room with ID " + sensor.getRoomId() + " does not exist."
            );
        }

        MockDatabase.SENSORS.put(sensor.getId(), sensor);

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<String>());
        }

        if (!room.getSensorIds().contains(sensor.getId())) {
            room.getSensorIds().add(sensor.getId());
        }

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(sensor.getId())
                .build();

        return Response.created(location)
                .entity(sensor)
                .build();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
