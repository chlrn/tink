package com.example.tink.service;
import com.example.tink.entity.TranslationEntity;
import com.example.tink.repository.TranslationRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TranslationServiceTest {

    private TranslationService translationService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TranslationRepository translationRepository;

    private ExecutorService executorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        executorService = Executors.newFixedThreadPool(10);
        translationService = new TranslationService(restTemplateBuilder, translationRepository);
    }

    @Test
    public void testTranslateWord() {
        String inputText = "angel";
        String translatedText = "{\"translations\":[{\"text\":\"ангел\"}]}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(translatedText, HttpStatus.OK);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        String result = translationService.translate(inputText, "en", "ru");

        assertEquals("ангел", result);
    }

    @Test
    public void testTranslateMultipleWords() {
        String inputText = "hello world";
        String translatedHello = "{\"translations\":[{\"text\":\"привет\"}]}";
        String translatedWorld = "{\"translations\":[{\"text\":\"мир\"}]}";

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenAnswer(invocation -> {
                    String body = invocation.getArgument(1, HttpEntity.class).getBody().toString();
                    if (body.contains("hello")) {
                        return new ResponseEntity<>(translatedHello, HttpStatus.OK);
                    } else if (body.contains("world")) {
                        return new ResponseEntity<>(translatedWorld, HttpStatus.OK);
                    }
                    return null;
                });

        String result = translationService.translate(inputText, "en", "ru");

        assertEquals("привет мир", result);
    }

    @Test
    public void testSaveTranslationRequest() {
        TranslationEntity translationEntity = new TranslationEntity();
        translationEntity.setIpAddress("127.0.0.1");
        translationEntity.setInputText("hello");
        translationEntity.setTranslatedText("привет");

        translationService.saveTranslationRequest(translationEntity);

        verify(translationRepository, times(1)).save(any(TranslationEntity.class));
    }
}
