package project.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.model.PageableDTO;
import project.model.locationModel.LocationAddressDTO;
import project.model.locationModel.LocationCoordinatesDTO;
import project.model.locationModel.LocationResponse;
import project.service.LocationService;

import java.util.List;
@Tag(name = "Location")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping( "/api/v1")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }
    @Operation(summary = "Get location coordinates",description = "Get location coordinates for showing locations on map")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = LocationCoordinatesDTO.class))}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @GetMapping("/locationCoordinates")
    ResponseEntity<?> getLocationCoordinates(){
        List<LocationCoordinatesDTO> locationCoordinatesDTOS = locationService.getLocationCoordinates();
        return new ResponseEntity<>(locationCoordinatesDTOS,HttpStatus.OK);
    }
    @Operation(summary = "Get location by id",description = "Get location by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = LocationResponse.class))}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Location not found",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @GetMapping("/location/{id}")
    ResponseEntity<LocationResponse> getLocation(@PathVariable("id")
                                                 @Parameter(name = "id", description = "Location id", example = "1")
                                                 Long id){
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(locationService.getLocationResponseById(id),HttpStatus.OK);
    }
    @Operation(summary = "Get locations list",description = "Get all locations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = LocationAddressDTO.class))}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @GetMapping("/locationsList")
    ResponseEntity<?> getLocationAddresses(PageableDTO pageableDTO){
        Page<LocationAddressDTO> locationAddressDTOS = locationService.getLocationAddresses(pageableDTO);
        return new ResponseEntity<>(locationAddressDTOS, HttpStatus.OK);
    }


}
