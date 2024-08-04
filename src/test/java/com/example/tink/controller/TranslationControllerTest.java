package com.example.tink.controller;

import com.example.tink.dto.TranslationRequest;
import com.example.tink.entity.TranslationEntity;
import com.example.tink.service.TranslationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TranslationControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private TranslationService translationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testTranslate() throws Exception {
        String inputText = "hello";
        String translatedText = "привет";

        TranslationRequest requestBody = new TranslationRequest();
        requestBody.setTexts(Collections.singletonList(inputText));
        requestBody.setSourceLanguageCode("en");
        requestBody.setTargetLanguageCode("ru");

        when(translationService.translate(anyString(), anyString(), anyString())).thenReturn(translatedText);

        TranslationEntity translationEntity = new TranslationEntity();
        translationEntity.setIpAddress("127.0.0.1");
        translationEntity.setInputText(inputText);
        translationEntity.setTranslatedText(translatedText);
        translationEntity.setTimestamp(LocalDateTime.now());

        doNothing().when(translationService).saveTranslationRequest(any(TranslationEntity.class));

        String requestJson = """
                {
                    "folderId": "b1gva6hkd65vrionjthb",
                    "texts": ["hello"],
                    "sourceLanguageCode": "en",
                    "targetLanguageCode": "ru"
                }
                """;

        mockMvc.perform(post("/api/translate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(translatedText));

        verify(translationService, times(1)).translate(inputText, "en", "ru");
        verify(translationService, times(1)).saveTranslationRequest(any(TranslationEntity.class));
    }
}
