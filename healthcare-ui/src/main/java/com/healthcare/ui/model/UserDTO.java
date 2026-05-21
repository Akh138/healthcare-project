package com.healthcare.ui.model;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String role; // "DOCTOR" ou "ADMIN" par exemple
}