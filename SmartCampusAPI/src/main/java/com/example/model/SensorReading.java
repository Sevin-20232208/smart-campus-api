/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

import java.util.Date;

/**
 *
 * @author Sevin Kawsika
 */

public class SensorReading {

    private String id;
    private String sensorId;
    private double value;
    private Date timestamp;

    public SensorReading() {
    }

    public SensorReading(String id, String sensorId, double value) {
        this.id = id;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = new Date(); // Automatically set the time of reading
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
