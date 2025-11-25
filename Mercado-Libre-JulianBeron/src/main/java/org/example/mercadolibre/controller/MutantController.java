package org.example.mercadolibre.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.mercadolibre.dto.DnaRequest;
import org.example.mercadolibre.dto.StatsResponse;
import org.example.mercadolibre.service.MutantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Tag(name = "Mutant Detection API", description = "API para detectar mutantes mediante análisis de ADN")
public class MutantController {

    @Autowired
    private MutantService mutantService;

    @GetMapping("/")
    @Operation(summary = "Página de inicio", description = "Redirige a la documentación de la API")
    public ResponseEntity<String> home() {
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Mutant Detection API</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 50px; background: #f0f0f0; }" +
                ".container { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); max-width: 600px; margin: 0 auto; }" +
                "h1 { color: #333; }" +
                "a { display: inline-block; margin: 10px 5px; padding: 12px 24px; background: #007bff; color: white; text-decoration: none; border-radius: 5px; }" +
                "a:hover { background: #0056b3; }" +
                ".endpoints { margin-top: 30px; background: #f8f9fa; padding: 20px; border-radius: 5px; }" +
                ".endpoint { margin: 10px 0; }" +
                ".method { font-weight: bold; color: #28a745; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>🧬 Mutant Detection API</h1>" +
                "<p>API REST para detectar mutantes mediante análisis de secuencias de ADN</p>" +
                "<div>" +
                "<a href='/swagger-ui/index.html'>📚 Documentación Swagger</a>" +
                "<a href='/stats'>📊 Ver Estadísticas</a>" +
                "</div>" +
                "<div class='endpoints'>" +
                "<h3>Endpoints disponibles:</h3>" +
                "<div class='endpoint'><span class='method'>POST</span> /mutant - Verificar si un ADN es mutante</div>" +
                "<div class='endpoint'><span class='method'>GET</span> /stats - Obtener estadísticas</div>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        return ResponseEntity.ok().header("Content-Type", "text/html; charset=UTF-8").body(html);
    }

    @PostMapping("/mutant")
    @Operation(
            summary = "Detectar si un ADN es mutante",
            description = "Recibe una secuencia de ADN y determina si pertenece a un mutante"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Es un mutante",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Es un mutante\"}"))),
            @ApiResponse(responseCode = "403", description = "No es un mutante",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"No es un mutante\"}"))),
            @ApiResponse(responseCode = "400", description = "ADN inválido",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"ADN inválido: debe ser una matriz NxN con solo caracteres A, T, C, G\"}")))
    })
    public ResponseEntity<?> isMutant(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = DnaRequest.class),
                            examples = @ExampleObject(name = "Ejemplo ADN mutante",
                                    value = "{\n  \"dna\": [\n    \"ATGCGA\",\n    \"CAGTGC\",\n    \"TTATGT\",\n    \"AGAAGG\",\n    \"CCCCTA\",\n    \"TCACTG\"\n  ]\n}")
                    )
            ) DnaRequest request) {
        try {
            boolean isMutant = mutantService.analyzeDna(request.getDna());

            if (isMutant) {
                return ResponseEntity.ok().body("{\"message\": \"Es un mutante\"}");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("{\"message\": \"No es un mutante\"}");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Error interno del servidor\"}");
        }
    }

    // Endpoint GET /stats - Retorna estadísticas de ADN verificados
    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas",
            description = "Devuelve estadísticas de las verificaciones de ADN")
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StatsResponse.class),
                    examples = @ExampleObject(value = "{\n  \"count_mutant_dna\": 40,\n  \"count_human_dna\": 100,\n  \"ratio\": 0.4\n}")))
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = mutantService.getStats();
        return ResponseEntity.ok(stats);
    }
}
