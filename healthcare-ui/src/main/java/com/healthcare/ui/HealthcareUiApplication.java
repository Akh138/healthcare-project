package com.healthcare.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient // Pour être vu par Consul
@EnableFeignClients    // Pour pouvoir appeler les microservices via des interfaces
public class HealthcareUiApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthcareUiApplication.class, args);
    }
}