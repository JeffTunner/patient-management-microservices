package com.pm.patient_service.service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.EmailAlreadyExistsException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO addPatient(PatientRequestDTO requestDTO) {
        if(patientRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A Patient with this email" + "already exists." + requestDTO.getEmail() );
        }
        Patient patient = PatientMapper.toPatient(requestDTO);
        Patient saved = patientRepository.save(patient);
        return PatientMapper.toDTO(saved);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found!" + id));
        if(patientRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A Patient with this email" + "already exists." + requestDTO.getEmail() );
        }

        patient.setName(requestDTO.getName());
        patient.setEmail(requestDTO.getEmail());
        patient.setAddress(requestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(requestDTO.getDateOfBirth()));

        Patient updated = patientRepository.save(patient);
        return PatientMapper.toDTO(updated);
    }
}
