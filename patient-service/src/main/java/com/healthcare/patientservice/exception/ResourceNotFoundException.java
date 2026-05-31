package com.healthcare.patientservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * C'EST MON ERREUR PERSONNALISÉE.
 * Elle sert à dire : "Désolé, ce que tu cherches n'existe pas en base de données".
 * On utilise @ResponseStatus pour que Spring renvoie automatiquement un code 404 (Not Found).
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        // On envoie le message personnalisé à la classe parente (RuntimeException)
        super(message);
    }
}