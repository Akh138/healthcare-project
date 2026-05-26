package com.healthcare.patientservice.controller;

import com.healthcare.patientservice.model.Appointment;
import com.healthcare.patientservice.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.createAppointment(appointment);
    }

    @GetMapping("/doctor/{username}")
    public List<Appointment> getAppointments(@PathVariable String username) {
        return appointmentService.getAppointmentsByDoctor(username);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }
}