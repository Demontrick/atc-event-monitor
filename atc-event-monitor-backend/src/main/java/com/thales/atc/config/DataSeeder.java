package com.thales.atc.config;

import com.thales.atc.model.*;
import com.thales.atc.repository.FlightEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final FlightEventRepository repository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<FlightEventDocument> events = List.of(
                create("AI101", Severity.CRITICAL, EventType.COLLISION_RISK),
                create("BA202", Severity.HIGH, EventType.AIRSPACE_BREACH),
                create("LH303", Severity.MEDIUM, EventType.FLIGHT_DELAYED),
                create("QR404", Severity.LOW, EventType.FLIGHT_NOMINAL),
                create("EK505", Severity.CRITICAL, EventType.EMERGENCY_DECLARED)
        );

        repository.saveAll(events);

        System.out.println("ATC SAMPLE EVENTS LOADED");
    }

    private FlightEventDocument create(String flightId, Severity severity, EventType type) {
        return FlightEventDocument.builder()
                .eventId(UUID.randomUUID().toString())
                .flightId(flightId)
                .aircraftType("A320")
                .altitude(35000)
                .heading(180)
                .eventType(type)
                .severity(severity)
                .timestamp(Instant.now())
                .location("EUROCONTROL AIRSPACE")
                .status(EventStatus.ACTIVE)
                .build();
    }
}