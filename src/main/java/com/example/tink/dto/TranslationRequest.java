package com.example.tink.dto;

import lombok.Data;

import java.util.List;

@Data
public class TranslationRequest {
    private String folderId;
    private List<String> texts;
    private String sourceLanguageCode;
    private String targetLanguageCode;
}
