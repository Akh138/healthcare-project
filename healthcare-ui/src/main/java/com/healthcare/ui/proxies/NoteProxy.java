package com.healthcare.ui.proxies;

import com.healthcare.ui.model.MessageDTO;
import com.healthcare.ui.model.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// On définit un seul client pour le microservice "note-service"
@FeignClient(name = "note-service", url = "http://localhost:8083")
public interface NoteProxy {

    // --- PARTIE NOTES MÉDICALES ---
    @GetMapping("/api/notes/patient/{patientId}")
    List<NoteDTO> getNotesByPatient(@PathVariable("patientId") Long patientId);

    @PostMapping("/api/notes")
    NoteDTO createNote(@RequestBody NoteDTO note);

    @DeleteMapping("/api/notes/{id}")
    void deleteNote(@PathVariable("id") String id);

    // --- PARTIE MESSAGERIE (CHAT) ---
    @GetMapping("/api/messages/patient/{patientId}")
    List<MessageDTO> getMessagesByPatient(@PathVariable("patientId") Long patientId);

    @PostMapping("/api/messages")
    MessageDTO sendMessage(@RequestBody MessageDTO message);
}