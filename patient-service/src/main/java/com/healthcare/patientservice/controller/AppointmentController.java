package com.healthcare.patientservice.controller;

import com.healthcare.patientservice.model.Appointment;
import com.healthcare.patientservice.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * MON API POUR LA GESTION DES RENDEZ-VOUS (PLANNING).
 * Comme pour les patients, j'utilise ResponseEntity pour maîtriser mes codes retour.
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // JE CRÉE UN NOUVEAU RENDEZ-VOUS
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Appointment created = appointmentService.createAppointment(appointment);
        // TOUCHE PRO : Je renvoie 201 Created pour dire que le RDV est bien enregistré
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // JE RÉCUPÈRE LE PLANNING D'UN MÉDECIN PRÉCIS
    @GetMapping("/doctor/{username}")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable String username) {
        List<Appointment> list = appointmentService.getAppointmentsByDoctor(username);
        // Je renvoie la liste avec un code 200 OK
        return ResponseEntity.ok(list);
    }

    // J'ANNULE (SUPPRIME) UN RENDEZ-VOUS
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        // TOUCHE PRO : Je renvoie 204 No Content car le RDV n'existe plus
        return ResponseEntity.noContent().build();
    }
}