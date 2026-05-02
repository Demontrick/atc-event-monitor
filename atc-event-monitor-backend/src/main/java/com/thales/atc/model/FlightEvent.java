package com.thales.atc.model;

import java.time.Instant;

public class FlightEvent {

    private String eventId;
    private String flightId;
    private String aircraftType;

    private double altitude;
    private double heading;

    private EventType eventType;
    private Severity severity;

    private Instant timestamp;
    private String location;

    public FlightEvent() {}

    public FlightEvent(String eventId,
                       String flightId,
                       String aircraftType,
                       double altitude,
                       double heading,
                       EventType eventType,
                       Severity severity,
                       Instant timestamp,
                       String location) {
        this.eventId = eventId;
        this.flightId = flightId;
        this.aircraftType = aircraftType;
        this.altitude = altitude;
        this.heading = heading;
        this.eventType = eventType;
        this.severity = severity;
        this.timestamp = timestamp;
        this.location = location;
    }

    // Getters & Setters

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }

    public String getAircraftType() { return aircraftType; }
    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }

    public double getAltitude() { return altitude; }
    public void setAltitude(double altitude) { this.altitude = altitude; }

    public double getHeading() { return heading; }
    public void setHeading(double heading) { this.heading = heading; }

    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}