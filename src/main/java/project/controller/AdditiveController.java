package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.model.additiveModel.AdditiveDTO;
import project.service.AdditiveService;

import java.util.List;
@Tag(name = "Additive")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
public class AdditiveController {
    private final AdditiveService additiveService;

    public AdditiveController(AdditiveService additiveService) {
        this.additiveService = additiveService;
    }
    @Operation(summary = "Get additives by additive type id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Additives not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/additives/{additiveTypeId}")
    ResponseEntity<?> getAdditivesForAdditiveType(@PathVariable("additiveTypeId")Long additiveTypeId){
        List<AdditiveDTO> additiveDTOS = additiveService.getAdditiveDTOsByAdditiveTypeId(additiveTypeId);
        if(additiveDTOS.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(additiveDTOS, HttpStatus.OK);
    }
}
