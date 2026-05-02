package com.thales.atc.controller;

import com.thales.atc.model.*;
import com.thales.atc.producer.FlightEventProducer;
import com.thales.atc.service.FlightEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights/events")
@RequiredArgsConstructor
public class FlightEventController {

    private final FlightEventService service;
    private final FlightEventProducer flightEventProducer;

    // GET ALL EVENTS
    @GetMapping
    public List<FlightEventDocument> getAll() {
        return service.getAllEvents();
    }

    // FILTER BY SEVERITY
    @GetMapping("/severity/{severity}")
    public List<FlightEventDocument> getBySeverity(@PathVariable Severity severity) {
        return service.getBySeverity(severity);
    }

    // FILTER BY TYPE
    @GetMapping("/type/{type}")
    public List<FlightEventDocument> getByType(@PathVariable EventType type) {
        return service.getByType(type);
    }

    // UPDATE STATUS (ACKNOWLEDGE / RESOLVE)
    @PatchMapping("/{id}/status")
    public FlightEventDocument updateStatus(
            @PathVariable String id,
            @RequestParam EventStatus status) {
        return service.updateStatus(id, status);
    }

    // DASHBOARD STATS
    @GetMapping("/stats/{severity}")
    public long countBySeverity(@PathVariable Severity severity) {
        return service.countBySeverity(severity);
    }

    // TEST EVENT PUSH TO KAFKA (ATC SIMULATION)
    @PostMapping("/publish/test")
    public String publishTestEvent() {
        flightEventProducer.publishRandomEvent();
        return "ATC EVENT SENT TO KAFKA";
    }
}