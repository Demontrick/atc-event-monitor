package com.thales.atc.consumer;

import com.thales.atc.config.KafkaConfig;
import com.thales.atc.model.FlightEvent;
import com.thales.atc.model.FlightEventDocument;
import com.thales.atc.model.EventStatus;
import com.thales.atc.repository.FlightEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FlightEventConsumer {

    private final FlightEventRepository repository;

    public FlightEventConsumer(FlightEventRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = KafkaConfig.TOPIC, groupId = "atc-group")
    public void consume(FlightEvent event) {

        FlightEventDocument doc = new FlightEventDocument();

        doc.setEventId(event.getEventId());
        doc.setFlightId(event.getFlightId());
        doc.setAircraftType(event.getAircraftType());
        doc.setAltitude(event.getAltitude());
        doc.setHeading(event.getHeading());
        doc.setEventType(event.getEventType());
        doc.setSeverity(event.getSeverity());
        doc.setTimestamp(event.getTimestamp());
        doc.setLocation(event.getLocation());

        doc.setStatus(EventStatus.ACTIVE);

        repository.save(doc);
    }
}