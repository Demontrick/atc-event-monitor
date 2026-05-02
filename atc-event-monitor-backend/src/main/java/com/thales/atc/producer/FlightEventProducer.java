package com.thales.atc.producer;

import com.thales.atc.model.EventType;
import com.thales.atc.model.FlightEvent;
import com.thales.atc.model.Severity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class FlightEventProducer {

    private static final String TOPIC = "flight-events";

    private final KafkaTemplate<String, FlightEvent> kafkaTemplate;
    private final Random random = new Random();

    private final List<String> flights = List.of(
            "AI101", "BA202", "LH303", "QR404", "EK505",
            "AF606", "DL707", "UA808", "SQ909", "TK010"
    );

    private final List<String> airspaces = List.of(
            "EUROCONTROL AIRSPACE",
            "INDIAN FIR",
            "ATLANTIC ROUTE",
            "MIDDLE EAST CORRIDOR"
    );

    public FlightEventProducer(KafkaTemplate<String, FlightEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishRandomEvent() {

        FlightEvent event = new FlightEvent();

        event.setEventId(UUID.randomUUID().toString());
        event.setFlightId(flights.get(random.nextInt(flights.size())));
        event.setAircraftType("A320");

        event.setAltitude(30000 + random.nextInt(10000));
        event.setHeading(random.nextInt(360));

        event.setEventType(EventType.values()[random.nextInt(EventType.values().length)]);
        event.setSeverity(Severity.values()[random.nextInt(Severity.values().length)]);

        event.setTimestamp(Instant.now());
        event.setLocation(airspaces.get(random.nextInt(airspaces.size())));

        kafkaTemplate.send(TOPIC, event.getFlightId(), event);
    }
}