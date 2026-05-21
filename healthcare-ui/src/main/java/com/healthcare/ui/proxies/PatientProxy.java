package com.healthcare.ui.proxies;

import com.healthcare.ui.model.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "patient-service", url = "http://localhost:8081")
public interface PatientProxy {

    @GetMapping("/api/patients")
    List<PatientDTO> getAllPatients();

    @PostMapping("/api/patients")
    PatientDTO createPatient(@RequestBody PatientDTO patient);
}