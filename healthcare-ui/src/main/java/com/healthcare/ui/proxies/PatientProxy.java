package com.healthcare.ui.proxies;

import com.healthcare.ui.model.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "patient-service", url = "http://localhost:8081", fallback = PatientFallback.class)
public interface PatientProxy {

    @GetMapping("/api/patients")
    List<PatientDTO> getAllPatients();


    @GetMapping("/api/patients/doctor/{username}")
    List<PatientDTO> getPatientsByDoctor(@PathVariable("username") String username);

    @GetMapping("/api/patients/{id}")
    PatientDTO getPatientById(@PathVariable("id") Long id);

    @PostMapping("/api/patients")
    PatientDTO createPatient(@RequestBody PatientDTO patient);

    @PutMapping("/api/patients/{id}")
    PatientDTO updatePatient(@PathVariable("id") Long id, @RequestBody PatientDTO patient);

    @DeleteMapping("/api/patients/{id}")
    void deletePatient(@PathVariable("id") Long id);

    @GetMapping("/api/patients/account/{username}")
    PatientDTO getPatientByAccount(@PathVariable("username") String username);
}