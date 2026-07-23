package com.pm.patient_service.kafka;

import com.pm.patient_service.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.event.PatientEvent;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    @Autowired
    KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(Patient patient) {
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        kafkaTemplate.send("patient", patientEvent.toByteArray())
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send PatientCreated event: {}", patientEvent, ex);
                    } else {
                        log.info("Sent PatientCreated event to partition {} at offset {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
