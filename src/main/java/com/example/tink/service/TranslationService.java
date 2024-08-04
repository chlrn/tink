package com.example.tink.service;

import com.example.tink.entity.TranslationEntity;
import com.example.tink.repository.TranslationRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class TranslationService {

    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final String apiKey = "AQVN11Q7idZUf5oaswdsv_nnZTiqkp2Us050iLSW";
    private final String folderId = "b1gva6hkd65vrionjthb";
    private final TranslationRepository translationRepository;

    @Autowired
    public TranslationService(RestTemplateBuilder restTemplateBuilder, TranslationRepository translationRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.executorService = Executors.newFixedThreadPool(10);
        this.translationRepository = translationRepository;
    }

    public String translate(String input, String sourceLanguageCode, String targetLanguageCode) {
        String[] words = input.split(" ");
        List<Future<String>> futures = new ArrayList<>();

        for (String word : words) {
            futures.add(executorService.submit(() -> translateWord(word, sourceLanguageCode, targetLanguageCode)));
        }

        StringBuilder translatedText = new StringBuilder();
        for (Future<String> future : futures) {
            try {
                translatedText.append(future.get()).append(" ");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return translatedText.toString().trim();
    }

    private String translateWord(String text, String sourceLanguageCode, String targetLanguageCode) {
        String apiUrl = "https://translate.api.cloud.yandex.net/translate/v2/translate";

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("folderId", folderId);
            requestBody.put("texts", new JSONArray().put(text));
            requestBody.put("targetLanguageCode", targetLanguageCode);
            requestBody.put("sourceLanguageCode", sourceLanguageCode);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Api-Key " + apiKey);

            HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseBody = new JSONObject(response.getBody());
                JSONArray translations = responseBody.getJSONArray("translations");
                if (translations.length() > 0) {
                    return translations.getJSONObject(0).getString("text");
                } else {
                    throw new RuntimeException("Translation not found in response");
                }
            } else {
                throw new RuntimeException("Failed to translate text: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create request body", e);
        }
    }

    public void saveTranslationRequest(TranslationEntity translationEntity) {
        translationRepository.save(translationEntity);
    }
}
