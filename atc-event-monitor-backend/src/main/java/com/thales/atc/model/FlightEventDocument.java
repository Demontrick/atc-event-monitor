package com.thales.atc.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "flight_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightEventDocument {

    @Id
    private String id;

    private String eventId;
    private String flightId;
    private String aircraftType;

    private double altitude;
    private double heading;

    private EventType eventType;
    private Severity severity;

    private Instant timestamp;
    private String location;

    private EventStatus status;
}