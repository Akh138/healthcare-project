package com.healthcare.patientservice.service;

import com.healthcare.patientservice.exception.ResourceNotFoundException; // J'importe mon erreur perso
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

    // Je récupère TOUS les patients (utile pour l'Admin)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Je récupère seulement les patients d'un docteur précis
    public List<Patient> getPatientsByDoctor(String username) {
        return patientRepository.findByDoctorUsername(username);
    }

    // Je cherche un patient par son ID unique
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // J'enregistre un nouveau patient dans MySQL
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // JE METS À JOUR LES INFOS D'UN PATIENT
    public Patient updatePatient(Long id, Patient patientDetails) {
        // TECHNIQUE PRO : Si l'ID n'existe pas, je lance tout de suite mon erreur 404
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient introuvable avec l'id : " + id));

        // Si on l'a trouvé, on met à jour tous les champs
        patient.setFirstName(patientDetails.getFirstName());
        patient.setLastName(patientDetails.getLastName());
        patient.setBirthday(patientDetails.getBirthday());
        patient.setGender(patientDetails.getGender());
        patient.setAddress(patientDetails.getAddress());
        patient.setPhone(patientDetails.getPhone());
        patient.setEmail(patientDetails.getEmail());
        patient.setProfilePicture(patientDetails.getProfilePicture());
        patient.setDoctorUsername(patientDetails.getDoctorUsername());
        patient.setPatientUsername(patientDetails.getPatientUsername());

        // Je sauvegarde les changements
        return patientRepository.save(patient);
    }

    // JE SUPPRIME UN PATIENT
    public void deletePatient(Long id) {
        // Je vérifie d'abord s'il existe en base pour ne pas faire d'erreur bête
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossible de supprimer : Patient non trouvé");
        }
        patientRepository.deleteById(id);
    }

    // Je cherche le profil d'un patient par son pseudo (pour son espace perso)
    public Optional<Patient> getPatientByAccount(String username) {
        return patientRepository.findByPatientUsername(username);
    }
}