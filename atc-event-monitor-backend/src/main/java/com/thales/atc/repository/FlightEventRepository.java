package com.thales.atc.repository;

import com.thales.atc.model.EventStatus;
import com.thales.atc.model.EventType;
import com.thales.atc.model.FlightEventDocument;
import com.thales.atc.model.Severity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FlightEventRepository extends MongoRepository<FlightEventDocument, String> {

    List<FlightEventDocument> findBySeverity(Severity severity);

    List<FlightEventDocument> findByEventType(EventType eventType);

    List<FlightEventDocument> findByStatus(EventStatus status);
}