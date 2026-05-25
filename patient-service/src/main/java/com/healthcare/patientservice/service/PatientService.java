package com.healthcare.patientservice.service;

import com.healthcare.patientservice.model.Patient;
import com.healthcare.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Récupérer TOUS les patients de la base (utile pour l'Admin plus tard)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // NOUVEAU : Récupérer seulement les patients d'un médecin précis
    public List<Patient> getPatientsByDoctor(String username) {
        return patientRepository.findByDoctorUsername(username);
    }

    // Trouver un patient par son numéro ID
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // Enregistrer un nouveau patient
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // Mettre à jour les infos d'un patient existant
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé"));

        patient.setFirstName(patientDetails.getFirstName());
        patient.setLastName(patientDetails.getLastName());
        patient.setBirthday(patientDetails.getBirthday());
        patient.setGender(patientDetails.getGender());
        patient.setAddress(patientDetails.getAddress());
        patient.setPhone(patientDetails.getPhone());
        patient.setEmail(patientDetails.getEmail());
        patient.setProfilePicture(patientDetails.getProfilePicture());

        // On n'oublie pas de garder le lien avec le médecin
        patient.setDoctorUsername(patientDetails.getDoctorUsername());

        return patientRepository.save(patient);
    }

    // Supprimer un patient
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public Optional<Patient> getPatientByAccount(String username) {
        return patientRepository.findByPatientUsername(username);
    }
}