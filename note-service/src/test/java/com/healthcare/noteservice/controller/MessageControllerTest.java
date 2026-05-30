package com.healthcare.noteservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.noteservice.model.Message;
import com.healthcare.noteservice.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ICI JE TESTE LES URLS DU CHAT (MESSAGERIE).
 */
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- TEST 1 : VÉRIFIER L'ENVOI D'UN MESSAGE ---
    @Test
    public void testSendMessage_ShouldReturnOk() throws Exception {
        Message msg = new Message("m-1", 1L, "Dr. Habib", "Comment allez-vous ?", null);
        when(messageService.saveMessage(any(Message.class))).thenReturn(msg);

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(msg)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Comment allez-vous ?"));
    }

    // --- TEST 2 : VÉRIFIER QU'ON RÉCUPÈRE LA DISCUSSION DU PATIENT ---
    @Test
    public void testGetMessagesByPatient_ShouldReturnList() throws Exception {
        when(messageService.getMessagesByPatient(1L)).thenReturn(Arrays.asList(new Message()));

        mockMvc.perform(get("/api/messages/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}