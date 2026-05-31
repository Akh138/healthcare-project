package com.healthcare.patientservice.controller;

import com.healthcare.patientservice.exception.ResourceNotFoundException;
import com.healthcare.patientservice.model.Patient;
import com.healthcare.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * MON API REST POUR LES PATIENTS.
 * Grâce au GlobalExceptionHandler, mes méthodes sont plus courtes et plus lisibles !
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Je renvoie la liste de tous les patients (200 OK)
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // Je renvoie les patients d'un médecin précis
    @GetMapping("/doctor/{username}")
    public ResponseEntity<List<Patient>> getPatientsByDoctor(@PathVariable String username) {
        return ResponseEntity.ok(patientService.getPatientsByDoctor(username));
    }

    // Je cherche un seul patient par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        // PLUS BESOIN DE IF/ELSE : Si c'est vide, l'erreur 404 est lancée par le service
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Ce patient n'existe pas"));
    }

    // Je crée un nouveau patient (201 Created)
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        Patient created = patientService.createPatient(patient);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Je mets à jour une fiche patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patientDetails) {
        // TOUCHE PRO : On a enlevé le try/catch !
        // Le surveillant s'occupera de l'erreur si le patient n'existe pas.
        return ResponseEntity.ok(patientService.updatePatient(id, patientDetails));
    }

    // Je supprime un patient (204 No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    // Je cherche une fiche via le nom de compte utilisateur
    @GetMapping("/account/{username}")
    public ResponseEntity<Patient> getPatientByAccount(@PathVariable String username) {
        return patientService.getPatientByAccount(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Compte patient introuvable"));
    }
}