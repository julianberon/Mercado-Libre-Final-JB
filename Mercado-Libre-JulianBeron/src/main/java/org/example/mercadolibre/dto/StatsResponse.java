package org.example.mercadolibre.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// DTO para respuesta de estadísticas
public class StatsResponse {

    @JsonProperty("count_mutant_dna")
    private long countMutantDna;

    @JsonProperty("count_human_dna")
    private long countHumanDna;

    @JsonProperty("ratio")
    private double ratio;

    public StatsResponse() {
    }

    public StatsResponse(long countMutantDna, long countHumanDna) {
        this.countMutantDna = countMutantDna;
        this.countHumanDna = countHumanDna;
        // Ratio = mutantes / humanos (0 si no hay humanos)
        this.ratio = countHumanDna == 0 ? 0 : (double) countMutantDna / countHumanDna;
    }

    public long getCountMutantDna() {
        return countMutantDna;
    }

    public void setCountMutantDna(long countMutantDna) {
        this.countMutantDna = countMutantDna;
    }

    public long getCountHumanDna() {
        return countHumanDna;
    }

    public void setCountHumanDna(long countHumanDna) {
        this.countHumanDna = countHumanDna;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}

