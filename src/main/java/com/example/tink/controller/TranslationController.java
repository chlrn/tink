package com.example.tink.controller;

import com.example.tink.dto.TranslationRequest;
import com.example.tink.entity.TranslationEntity;
import com.example.tink.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/translate")
public class TranslationController {

    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    public ResponseEntity<String> translate(@RequestBody TranslationRequest requestBody, HttpServletRequest request) {
        String translatedText = translationService.translate(
                requestBody.getTexts().get(0),
                requestBody.getSourceLanguageCode(),
                requestBody.getTargetLanguageCode()
        );

        TranslationEntity translationEntity = new TranslationEntity();
        translationEntity.setIpAddress(request.getRemoteAddr());
        translationEntity.setInputText(requestBody.getTexts().get(0));
        translationEntity.setTranslatedText(translatedText);
        translationEntity.setTimestamp(LocalDateTime.now());
        translationService.saveTranslationRequest(translationEntity);

        return ResponseEntity.ok(translatedText);
    }
}
