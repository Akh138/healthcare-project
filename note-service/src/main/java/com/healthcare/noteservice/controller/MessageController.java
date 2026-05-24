package com.healthcare.noteservice.controller;

import com.healthcare.noteservice.model.Message;
import com.healthcare.noteservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages") // Route différente de /api/notes
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        return messageService.saveMessage(message);
    }

    @GetMapping("/patient/{patientId}")
    public List<Message> getMessagesByPatient(@PathVariable Long patientId) {
        return messageService.getMessagesByPatient(patientId);
    }
}