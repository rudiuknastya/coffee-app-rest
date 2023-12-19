package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.model.additiveModel.AdditiveDTO;
import project.service.AdditiveService;

import java.util.List;
@Tag(name = "Additive")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping( "/api/v1")
public class AdditiveController {
    private final AdditiveService additiveService;

    public AdditiveController(AdditiveService additiveService) {
        this.additiveService = additiveService;
    }
    @Operation(summary = "Get additives by additive type id",description = "Get additives by additive type id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = AdditiveDTO.class))}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @GetMapping("/additives/{additiveTypeId}")
    ResponseEntity<List<AdditiveDTO>> getAdditivesForAdditiveType( @PathVariable("additiveTypeId")
                                                                   @Parameter(name = "additiveTypeId", description = "Additive type id", example = "1")
                                                                   Long additiveTypeId){
        List<AdditiveDTO> additiveDTOS = additiveService.getAdditiveDTOsByAdditiveTypeId(additiveTypeId);
        return new ResponseEntity<>(additiveDTOS, HttpStatus.OK);
    }
}
