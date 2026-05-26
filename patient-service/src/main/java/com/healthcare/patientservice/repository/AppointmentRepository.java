package com.healthcare.patientservice.repository;

import com.healthcare.patientservice.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Pour récupérer le planning d'un médecin précis, trié par date
    List<Appointment> findByDoctorUsernameOrderByDateTimeAsc(String doctorUsername);
}