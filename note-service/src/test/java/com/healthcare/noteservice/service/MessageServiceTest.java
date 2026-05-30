package com.healthcare.noteservice.service;

import com.healthcare.noteservice.model.Message;
import com.healthcare.noteservice.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TEST UNITAIRE POUR LA MESSAGERIE (MongoDB).
 * On vérifie que les messages du chat s'enregistrent bien.
 */
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    // --- TEST 1 : VOIR LA DISCUSSION D'UN PATIENT ---
    @Test
    public void testGetMessagesByPatient_ShouldReturnList() {
        // 1. PRÉPARER
        when(messageRepository.findByPatientIdOrderByTimestampAsc(1L))
                .thenReturn(Arrays.asList(new Message(), new Message()));

        // 2. AGIR
        List<Message> result = messageService.getMessagesByPatient(1L);

        // 3. VÉRIFIER
        assertEquals(2, result.size());
        verify(messageRepository, times(1)).findByPatientIdOrderByTimestampAsc(1L);
    }

    // --- TEST 2 : ENVOYER UN MESSAGE ---
    @Test
    public void testSaveMessage_ShouldReturnSavedMessage() {
        // 1. PRÉPARER
        Message msg = new Message();
        msg.setContent("Bonjour Docteur");

        Message savedMsg = new Message();
        savedMsg.setId("msg-999");
        savedMsg.setContent("Bonjour Docteur");

        when(messageRepository.save(any(Message.class))).thenReturn(savedMsg);

        // 2. AGIR
        Message result = messageService.saveMessage(msg);

        // 3. VÉRIFIER
        assertNotNull(result.getId());
        assertNotNull(result.getTimestamp()); // On vérifie que la date d'envoi a été ajoutée
        verify(messageRepository, times(1)).save(any(Message.class));
    }
}