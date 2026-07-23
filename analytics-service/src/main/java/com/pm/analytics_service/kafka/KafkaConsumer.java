package com.pm.analytics_service.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import patient.event.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics="patient", groupId = "analytics-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeEvent(byte[] event, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                             @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("=== LISTENER INVOKED === Partition: {}, Offset: {}", partition, offset);

        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            log.info("Received Patient Event: [PatientId={}, Name={}, Email={}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail());
        } catch (InvalidProtocolBufferException e) {
            log.error("Failed to parse protobuf message at offset {}", offset, e);
        }
    }
}
