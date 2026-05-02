package com.thales.atc.service;

import com.thales.atc.model.*;
import com.thales.atc.repository.FlightEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightEventService {

    private final FlightEventRepository repository;

    // GET ALL EVENTS
    public List<FlightEventDocument> getAllEvents() {
        return repository.findAll();
    }

    // FILTER BY SEVERITY
    public List<FlightEventDocument> getBySeverity(Severity severity) {
        return repository.findBySeverity(severity);
    }

    // FILTER BY TYPE
    public List<FlightEventDocument> getByType(EventType type) {
        return repository.findByEventType(type);
    }

    // UPDATE STATUS (ACK / RESOLVE)
    public FlightEventDocument updateStatus(String id, EventStatus status) {

        FlightEventDocument event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight event not found: " + id));

        event.setStatus(status);
        return repository.save(event);
    }

    // SIMPLE STATS (for dashboard later)
    public long countBySeverity(Severity severity) {
        return repository.findBySeverity(severity).size();
    }
}