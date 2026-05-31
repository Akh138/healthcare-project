package com.healthcare.patientservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * C'EST MON "SURVEILLANT GÉNÉRAL" (TRIEUR D'ERREURS).
 * Ce fichier surveille tout ce qui se passe dans mon microservice.
 * Si une erreur arrive, il l'attrape et la transforme en un joli message JSON propre.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // Ici, j'attrape mon erreur "Pas trouvé" (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        // Je prépare une petite boîte (Map) pour mon message d'erreur
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now()); // Je mets l'heure de l'erreur
        body.put("message", ex.getMessage());       // Je mets le message d'erreur (ex: Patient non trouvé)

        // Je renvoie le tout au format JSON avec le code 404
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Ici, j'attrape toutes les autres erreurs imprévues (500) pour pas que ça crash violemment
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalError(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Une erreur technique est survenue sur le serveur");

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}