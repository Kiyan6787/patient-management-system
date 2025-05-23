package com.patient.manager.analytics_service.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consume(byte[] event) {

        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            //Perform business logic related to analytics here
            //Calling service layer, DB, etc.

            log.info("Received patient event: [PatientID={}, PatientName={} ]", patientEvent.getPatientId(), patientEvent.getName());
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event {}", e.getMessage());
        } catch (Exception e) {
            log.error("An error has occurred in KafkaConsumer");
        }
    }
}
