package com.challenge.literAlura.repository;

import com.challenge.literAlura.model.AutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<AutorEntity, Long> {
    Optional<AutorEntity> findByNameContains(String name);
    List<AutorEntity> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(Integer birthYear, Integer deathYear);
}
