package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.model.additiveModel.AdditiveDTO;
import project.service.AdditiveService;

import java.util.List;
@Tag(name = "Additive")
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
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/additives/{additiveTypeId}")
    List<AdditiveDTO> getAdditivesForAdditiveType(@PathVariable("additiveTypeId")Long additiveTypeId){
        return additiveService.getAdditiveDTOsByAdditiveTypeId(additiveTypeId);
    }
}
