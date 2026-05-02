package com.thales.atc.model;

import java.time.Instant;

public record FlightEvent(
        String eventId,
        String flightId,
        String aircraftType,
        double altitude,
        double heading,
        EventType eventType,
        Severity severity,
        Instant timestamp,
        String location
) {}