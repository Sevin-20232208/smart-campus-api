/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.resource;

/**
 *
 * @author Sevin Kawsika
 */
import com.example.dao.MockDatabase;
import com.example.exception.DataNotFoundException;
import com.example.exception.RoomNotEmptyException;
import com.example.model.ErrorMessage;
import com.example.model.Room;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {

    @GET
    public List<Room> getAllRooms() {
        return new ArrayList<>(MockDatabase.ROOMS.values());
    }

    @GET
    @Path("/{roomId}")
    public Room getRoomById(@PathParam("roomId") String roomId) {
        Room room = MockDatabase.ROOMS.get(roomId);

        if (room == null) {
            throw new DataNotFoundException("Room with ID " + roomId + " not found.");
        }

        return room;
    }

    @POST
    public Response addRoom(Room room, @Context UriInfo uriInfo) {
        if (room == null || room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Room ID is required.", 400))
                    .build();
        }

        if (MockDatabase.ROOMS.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorMessage("Room with ID " + room.getId() + " already exists.", 409))
                    .build();
        }

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<String>());
        }

        MockDatabase.ROOMS.put(room.getId(), room);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(room.getId())
                .build();

        return Response.created(location)
                .entity(room)
                .build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = MockDatabase.ROOMS.get(roomId);

        if (room == null) {
            throw new DataNotFoundException("Room with ID " + roomId + " not found.");
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                    "Room " + roomId + " cannot be deleted because it still has sensors assigned."
            );
        }

        MockDatabase.ROOMS.remove(roomId);
        return Response.noContent().build();
    }
}
