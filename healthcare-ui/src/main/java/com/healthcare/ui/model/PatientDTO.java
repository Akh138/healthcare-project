package com.healthcare.ui.model;

import lombok.Data;

@Data
public class PatientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String birthday;
    private String address;
    private String phone;
    private String gender;
    private String email;
}