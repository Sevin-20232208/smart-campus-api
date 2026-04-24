/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dao;

import com.example.model.Room;
import com.example.model.Sensor;
import com.example.model.SensorReading;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Sevin Kawsika
 */
public class MockDatabase {

    public static final Map<String, Room> ROOMS = new ConcurrentHashMap<>();
    public static final Map<String, Sensor> SENSORS = new ConcurrentHashMap<>();
    public static final Map<String, List<SensorReading>> READINGS = new ConcurrentHashMap<>();

    static {
        ROOMS.put("LIB-301",
                new Room(
                        "LIB-301",
                        "Library Quiet Study",
                        120,
                        new ArrayList<>(Arrays.asList("TEMP-001"))
                )
        );

        ROOMS.put("LAB-201",
                new Room(
                        "LAB-201",
                        "Computer Lab 201",
                        40,
                        new ArrayList<>()
                )
        );

        SENSORS.put("TEMP-001",
                new Sensor("TEMP-001", "Temperature", "ACTIVE", 21.5, "LIB-301")
        );
        
        READINGS.put("TEMP-001", new ArrayList<>());
        READINGS.put("CO2-001", new ArrayList<>());
    }

    private MockDatabase() {
    }

}
