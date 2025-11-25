package org.example.mercadolibre.repository;

import org.example.mercadolibre.entity.Dna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio JPA para acceso a datos de ADN
@Repository
public interface DnaRepository extends JpaRepository<Dna, Long> {

    Optional<Dna> findByDnaHash(String dnaHash);

    long countByIsMutant(boolean isMutant);

    boolean existsByDnaHash(String dnaHash);
}
