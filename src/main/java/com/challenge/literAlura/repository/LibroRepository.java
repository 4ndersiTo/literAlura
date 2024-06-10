package com.challenge.literAlura.repository;

import com.challenge.literAlura.model.LibroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<LibroEntity, Long> {
    Optional<LibroEntity> findByTitleContains(String title);
    List<LibroEntity> findByLanguageContains(String language);
}
