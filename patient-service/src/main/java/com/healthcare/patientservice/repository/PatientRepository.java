package com.healthcare.patientservice.repository;

import com.healthcare.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Cette méthode magique de Spring Data va créer automatiquement
    // la requête SQL : SELECT * FROM patients WHERE doctor_username = ?
    List<Patient> findByDoctorUsername(String doctorUsername);
}