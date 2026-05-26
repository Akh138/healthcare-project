package com.healthcare.patientservice.service;

import com.healthcare.patientservice.model.Appointment;
import com.healthcare.patientservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorUsername) {
        return appointmentRepository.findByDoctorUsernameOrderByDateTimeAsc(doctorUsername);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}