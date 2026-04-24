/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.config;

import com.example.exception.DataNotFoundExceptionMapper;
import com.example.exception.RoomNotEmptyExceptionMapper;
import com.example.exception.LinkedResourceNotFoundExceptionMapper;
import com.example.exception.SensorUnavailableExceptionMapper;
import com.example.exception.GenericExceptionMapper;
import com.example.exception.WebApplicationExceptionMapper;
import com.example.exception.LoggingFilter;
import com.example.resource.DiscoveryResource;
import com.example.resource.SensorResource;
import com.example.resource.SensorRoomResource;
import com.example.resource.SensorReadingResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        classes.add(DiscoveryResource.class);
        classes.add(SensorRoomResource.class);
        classes.add(SensorResource.class);
        classes.add(SensorReadingResource.class);

        // Add exception mappers
        classes.add(DataNotFoundExceptionMapper.class);
        classes.add(RoomNotEmptyExceptionMapper.class);
        classes.add(LinkedResourceNotFoundExceptionMapper.class);
        classes.add(SensorUnavailableExceptionMapper.class);
        classes.add(GenericExceptionMapper.class);
        classes.add(WebApplicationExceptionMapper.class);
        classes.add(LoggingFilter.class);

        return classes;
    }
}