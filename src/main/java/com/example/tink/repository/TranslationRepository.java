package com.example.tink.repository;

import com.example.tink.entity.TranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepository extends JpaRepository<TranslationEntity, Long> {
}
