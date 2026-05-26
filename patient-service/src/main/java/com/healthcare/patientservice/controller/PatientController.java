package com.healthcare.patientservice.controller;

import com.healthcare.patientservice.model.Patient;
import com.healthcare.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C'EST LE CONTRÔLEUR REST POUR LES PATIENTS.
 * Il permet de faire toutes les opérations (CRUD) via des requêtes HTTP.
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // J'AFFICHE TOUS LES PATIENTS DU SYSTÈME (UTILE POUR L'ADMIN)
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        // On renvoie un code 200 OK avec la liste
        return ResponseEntity.ok(patients);
    }

    // J'AFFICHE LES PATIENTS D'UN MÉDECIN PRÉCIS
    @GetMapping("/doctor/{username}")
    public ResponseEntity<List<Patient>> getPatientsByDoctor(@PathVariable String username) {
        List<Patient> patients = patientService.getPatientsByDoctor(username);
        return ResponseEntity.ok(patients);
    }

    // JE RÉCUPÈRE LES INFOS D'UN SEUL PATIENT PAR SON ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok) // Si trouvé -> 200 OK
                .orElse(ResponseEntity.notFound().build()); // Sinon -> 404 Not Found
    }

    // JE CRÉE UN NOUVEAU PATIENT
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        // TOUCHE PRO : On renvoie un code 201 (CREATED) au lieu de 200
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    // JE METS À JOUR LA FICHE D'UN PATIENT
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patientDetails) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patientDetails);
            return ResponseEntity.ok(updatedPatient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // JE SUPPRIME UN PATIENT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        // TOUCHE PRO : On renvoie 204 (NO CONTENT) car il n'y a plus rien à afficher
        return ResponseEntity.noContent().build();
    }

    // JE CHERCHE LE PROFIL D'UN PATIENT PAR SON NOM DE COMPTE
    @GetMapping("/account/{username}")
    public ResponseEntity<Patient> getPatientByAccount(@PathVariable String username) {
        return patientService.getPatientByAccount(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}