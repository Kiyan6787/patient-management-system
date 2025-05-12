package com.patient.manager.patient_service.service;

import com.patient.manager.patient_service.dto.PatientRequestDTO;
import com.patient.manager.patient_service.dto.PatientResponseDTO;
import com.patient.manager.patient_service.exception.EmailAlreadyExistsException;
import com.patient.manager.patient_service.exception.PatientNotFoundException;
import com.patient.manager.patient_service.mapper.PatientMapper;
import com.patient.manager.patient_service.model.Patient;
import com.patient.manager.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("The email address " + patientRequestDTO.getEmail() + " is already registered");
        }

        var newPatient = patientRepository.save(PatientMapper.toEntity(patientRequestDTO));

        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient currentPatient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient with the ID: " + id + " not found"));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)){
            throw new EmailAlreadyExistsException("The email address " + patientRequestDTO.getEmail() + " is already registered");
        }

        currentPatient.setName(patientRequestDTO.getName());
        currentPatient.setAddress(patientRequestDTO.getAddress());
        currentPatient.setEmail(patientRequestDTO.getEmail());
        currentPatient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        var updatedPatient = patientRepository.save(currentPatient);

        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
