package org.example.mercadolibre.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// DTO para recibir secuencias de ADN en el endpoint /mutant
public class DnaRequest {

    @NotNull(message = "El ADN no puede ser nulo")
    @Size(min = 4, message = "El ADN debe tener al menos 4 secuencias")
    private String[] dna;

    public DnaRequest() {
    }

    public DnaRequest(String[] dna) {
        this.dna = dna;
    }

    public String[] getDna() {
        return dna;
    }

    public void setDna(String[] dna) {
        this.dna = dna;
    }
}

