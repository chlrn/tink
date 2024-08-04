package com.example.tink.repository;

import com.example.tink.entity.TranslationEntity;
import com.example.tink.repository.TranslationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TranslationRepositoryTest {

    @Autowired
    private TranslationRepository translationRepository;

    @Test
    public void testSaveAndFind() {
        TranslationEntity entity = new TranslationEntity();
        entity.setIpAddress("127.0.0.1");
        entity.setInputText("hello");
        entity.setTranslatedText("привет");
        entity.setTimestamp(LocalDateTime.now());

        TranslationEntity savedEntity = translationRepository.save(entity);
        TranslationEntity foundEntity = translationRepository.findById(savedEntity.getId()).orElse(null);

        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getTranslatedText()).isEqualTo("привет");
    }
}
