package mercadolibre.service;

import org.example.mercadolibre.entity.Dna;
import org.example.mercadolibre.repository.DnaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private DnaRepository dnaRepository;

    @InjectMocks
    private MutantService mutantService;

    @BeforeEach
    void setUp() {
        // Setup común para todos los tests
    }

    @Test
    void testIsMutant_WithMutantDna_ShouldReturnTrue() {
        // Caso de ejemplo del enunciado - Es mutante
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertTrue(mutantService.isMutant(dna), "Debería detectar ADN mutante");
    }

    @Test
    void testIsMutant_WithHumanDna_ShouldReturnFalse() {
        // ADN humano sin secuencias repetidas
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATTT",
            "AGACGG",
            "GCGTCA",
            "TCACTG"
        };

        assertFalse(mutantService.isMutant(dna), "No debería detectar ADN mutante");
    }

    @Test
    void testIsMutant_WithHorizontalSequences_ShouldReturnTrue() {
        // 2 secuencias horizontales
        String[] dna = {
            "AAAATG",
            "TGCAGT",
            "GCTTTT",
            "CGATCT",
            "AGTACG",
            "TGACTA"
        };

        assertTrue(mutantService.isMutant(dna), "Debería detectar secuencias horizontales");
    }

    @Test
    void testIsMutant_WithVerticalSequences_ShouldReturnTrue() {
        // 2 secuencias verticales
        String[] dna = {
            "ATGCGA",
            "ATGTGC",
            "ATATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertTrue(mutantService.isMutant(dna), "Debería detectar secuencias verticales");
    }

    @Test
    void testIsMutant_WithDiagonalSequences_ShouldReturnTrue() {
        // 2 secuencias diagonales
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertTrue(mutantService.isMutant(dna), "Debería detectar secuencias diagonales");
    }

    @Test
    void testIsMutant_WithOnlyOneSequence_ShouldReturnFalse() {
        // Solo 1 secuencia (necesita más de 1 para ser mutante)
        String[] dna = {
            "AAAATG",
            "TGCAGT",
            "GCTTCT",
            "CGATCT",
            "AGTACG",
            "TGACTA"
        };

        assertFalse(mutantService.isMutant(dna), "Con solo 1 secuencia no es mutante");
    }

    @Test
    void testIsMutant_WithNullDna_ShouldReturnFalse() {
        assertFalse(mutantService.isMutant(null), "ADN nulo debería retornar false");
    }

    @Test
    void testIsMutant_WithSmallMatrix_ShouldReturnFalse() {
        String[] dna = {"ATG", "CAG", "TTA"};
        assertFalse(mutantService.isMutant(dna), "Matriz menor a 4x4 debería retornar false");
    }

    @Test
    void testIsMutant_WithInvalidCharacters_ShouldThrowException() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATXT",  // X es inválido
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertThrows(IllegalArgumentException.class,
            () -> mutantService.isMutant(dna),
            "Debería lanzar excepción con caracteres inválidos");
    }

    @Test
    void testIsMutant_WithNonSquareMatrix_ShouldThrowException() {
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTAT",    // Fila más corta
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        assertThrows(IllegalArgumentException.class,
            () -> mutantService.isMutant(dna),
            "Debería lanzar excepción si no es cuadrada");
    }

    @Test
    void testAnalyzeDna_WithNewDna_ShouldSaveToDatabase() {
        // Arrange
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        when(dnaRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(dnaRepository.save(any(Dna.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        boolean result = mutantService.analyzeDna(dna);

        // Assert
        assertTrue(result);
        verify(dnaRepository, times(1)).save(any(Dna.class));
    }

    @Test
    void testAnalyzeDna_WithExistingDna_ShouldNotSaveAgain() {
        // Arrange
        String[] dna = {
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        };

        Dna existingDna = new Dna("hash123", true, "dna");
        when(dnaRepository.findByDnaHash(anyString())).thenReturn(Optional.of(existingDna));

        // Act
        boolean result = mutantService.analyzeDna(dna);

        // Assert
        assertTrue(result);
        verify(dnaRepository, never()).save(any(Dna.class));
    }

    @Test
    void testGetStats_ShouldReturnCorrectStatistics() {
        // Arrange
        when(dnaRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRepository.countByIsMutant(false)).thenReturn(100L);

        // Act
        var stats = mutantService.getStats();

        // Assert
        assertNotNull(stats);
        assertEquals(40L, stats.getCountMutantDna());
        assertEquals(100L, stats.getCountHumanDna());
        assertEquals(0.4, stats.getRatio(), 0.001);
    }

    @Test
    void testGetStats_WithNoHumans_ShouldReturnZeroRatio() {
        // Arrange
        when(dnaRepository.countByIsMutant(true)).thenReturn(10L);
        when(dnaRepository.countByIsMutant(false)).thenReturn(0L);

        // Act
        var stats = mutantService.getStats();

        // Assert
        assertEquals(0.0, stats.getRatio());
    }
}

