package org.example.mercadolibre.service;

import org.example.mercadolibre.entity.Dna;
import org.example.mercadolibre.repository.DnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

@Service
public class MutantService {

    @Autowired
    private DnaRepository dnaRepository;

    public boolean isMutant(String[] dna) {
        if (dna == null || dna.length < 4) {
            return false;
        }

        int n = dna.length;

        for (String row : dna) {
            if (row == null || row.length() != n || !row.matches("[ATCG]+")) {
                throw new IllegalArgumentException("ADN inválido: debe ser una matriz NxN con solo caracteres A, T, C, G");
            }
        }

        int sequencesFound = 0;

        // Horizontales
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n - 4; j++) {
                if (hasSequence(dna[i].substring(j, j + 4))) {
                    sequencesFound++;
                    if (sequencesFound > 1) return true;
                }
            }
        }

        // Verticales
        for (int j = 0; j < n; j++) {
            for (int i = 0; i <= n - 4; i++) {
                StringBuilder vertical = new StringBuilder();
                for (int k = 0; k < 4; k++) {
                    vertical.append(dna[i + k].charAt(j));
                }
                if (hasSequence(vertical.toString())) {
                    sequencesFound++;
                    if (sequencesFound > 1) return true;
                }
            }
        }

        // Diagonales descendentes ↘
        for (int i = 0; i <= n - 4; i++) {
            for (int j = 0; j <= n - 4; j++) {
                StringBuilder diagonal = new StringBuilder();
                for (int k = 0; k < 4; k++) {
                    diagonal.append(dna[i + k].charAt(j + k));
                }
                if (hasSequence(diagonal.toString())) {
                    sequencesFound++;
                    if (sequencesFound > 1) return true;
                }
            }
        }

        // Diagonales ascendentes ↗
        for (int i = 3; i < n; i++) {
            for (int j = 0; j <= n - 4; j++) {
                StringBuilder diagonal = new StringBuilder();
                for (int k = 0; k < 4; k++) {
                    diagonal.append(dna[i - k].charAt(j + k));
                }
                if (hasSequence(diagonal.toString())) {
                    sequencesFound++;
                    if (sequencesFound > 1) return true;
                }
            }
        }

        return false;
    }

    private boolean hasSequence(String sequence) {
        if (sequence.length() != 4) return false;
        char first = sequence.charAt(0);
        return sequence.equals(String.valueOf(first).repeat(4));
    }

    public boolean analyzeDna(String[] dna) {
        String dnaHash = generateHash(dna);

        Optional<Dna> existingDna = dnaRepository.findByDnaHash(dnaHash);
        if (existingDna.isPresent()) {
            return existingDna.get().isMutant();
        }

        boolean isMutant = isMutant(dna);

        Dna dnaEntity = new Dna(dnaHash, isMutant, Arrays.toString(dna));
        dnaRepository.save(dnaEntity);

        return isMutant;
    }

    public org.example.mercadolibre.dto.StatsResponse getStats() {
        long countMutant = dnaRepository.countByIsMutant(true);
        long countHuman = dnaRepository.countByIsMutant(false);

        return new org.example.mercadolibre.dto.StatsResponse(countMutant, countHuman);
    }

    // Genera hash SHA-256 del ADN para identificarlo de forma única
    private String generateHash(String[] dna) {
        try {
            String dnaString = String.join("", dna);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dnaString.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash del ADN", e);
        }
    }
}

