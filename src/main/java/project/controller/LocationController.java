package project.controller;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }
    @Operation(summary = "Get location coordinates for showing locations on map")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Location coordinates not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/locationCoordinates")
    ResponseEntity<?> getLocationCoordinates(){
        List<LocationCoordinatesDTO> locationCoordinatesDTOS = locationService.getLocationCoordinates();
        if(locationCoordinatesDTOS.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(locationCoordinatesDTOS,HttpStatus.OK);
    }
    @Operation(summary = "Get location by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/location/{id}")
    ResponseEntity<LocationResponse> getLocation(@PathVariable("id")Long id){
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(locationService.getLocationResponseById(id),HttpStatus.OK);
    }
    @Operation(summary = "Get locations list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Locations not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/locationsList")
    ResponseEntity<?> getLocationAddresses(PageableDTO pageableDTO){
        Pageable pageable;
        Sort sort;
        if(pageableDTO.getSortDirection().equals("DESC")){
            sort = Sort.by(pageableDTO.getSortField()).descending();
        }
        else{
            sort = Sort.by(pageableDTO.getSortField()).ascending();
        }
        pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getSize(),sort);
        Page<LocationAddressDTO> locationAddressDTOS = locationService.getLocationAddresses(pageable);
        if(locationAddressDTOS.getContent().size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(locationAddressDTOS, HttpStatus.OK);
    }


}
