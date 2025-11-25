package org.example.mercadolibre.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dna_records")
public class Dna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", nullable = false, unique = true, length = 64)
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private boolean isMutant;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "dna_sequence", columnDefinition = "TEXT")
    private String dnaSequence;

    public Dna() {
        this.createdAt = LocalDateTime.now();
    }

    public Dna(String dnaHash, boolean isMutant, String dnaSequence) {
        this.dnaHash = dnaHash;
        this.isMutant = isMutant;
        this.dnaSequence = dnaSequence;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDnaHash() {
        return dnaHash;
    }

    public void setDnaHash(String dnaHash) {
        this.dnaHash = dnaHash;
    }

    public boolean isMutant() {
        return isMutant;
    }

    public void setMutant(boolean mutant) {
        isMutant = mutant;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDnaSequence() {
        return dnaSequence;
    }

    public void setDnaSequence(String dnaSequence) {
        this.dnaSequence = dnaSequence;
    }
}