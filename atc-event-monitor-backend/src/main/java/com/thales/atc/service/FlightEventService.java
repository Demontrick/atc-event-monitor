package com.thales.atc.service;

import com.thales.atc.model.*;
import com.thales.atc.repository.FlightEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlightEventService {

    private final FlightEventRepository repository;

    // =========================
    // GET ALL EVENTS
    // =========================
    public List<FlightEventDocument> getAllEvents() {
        return repository.findAll();
    }

    // =========================
    // FILTER BY SEVERITY
    // =========================
    public List<FlightEventDocument> getBySeverity(Severity severity) {
        return repository.findBySeverity(severity);
    }

    // =========================
    // FILTER BY TYPE
    // =========================
    public List<FlightEventDocument> getByType(EventType type) {
        return repository.findByEventType(type);
    }

    // =========================
    // UPDATE STATUS (ACK / RESOLVE)
    // =========================
    public FlightEventDocument updateStatus(String id, EventStatus status) {

        FlightEventDocument event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight event not found: " + id));

        event.setStatus(status);
        return repository.save(event);
    }

    // =========================
    // STATS FOR DASHBOARD (FIXED)
    // =========================
    public Map<String, Long> getStats() {

        List<FlightEventDocument> events = repository.findAll();

        Map<String, Long> stats = new HashMap<>();

        // initialize all severities to 0 (IMPORTANT for frontend stability)
        for (Severity s : Severity.values()) {
            stats.put(s.name(), 0L);
        }

        // count events
        for (FlightEventDocument e : events) {
            String key = e.getSeverity().name();
            stats.put(key, stats.get(key) + 1);
        }

        return stats;
    }

    // =========================
    // OPTIONAL LEGACY METHOD (KEEP FOR BACKWARD COMPATIBILITY)
    // =========================
    public long countBySeverity(Severity severity) {
        return repository.findBySeverity(severity).size();
    }
}