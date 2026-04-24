/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.resource;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getDiscovery(@Context UriInfo uriInfo) {
        String base = uriInfo.getBaseUri().toString();

        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("api", "Smart Campus API");
        response.put("version", "v1");

        Map<String, String> contact = new LinkedHashMap<>();
        contact.put("adminName", "Sevin Kawsika");
        contact.put("adminEmail", "w2120210@westminster.ac.uk");
        response.put("contact", contact);

        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("self", base);
        resources.put("rooms", base + "/rooms");
        resources.put("sensors", base + "/sensors");
        resources.put("readings", base + "/readings");
        response.put("resources", resources);

        return response;
    }
}
