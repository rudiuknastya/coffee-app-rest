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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.model.PageableDTO;
import project.model.locationModel.LocationAddressDTO;
import project.model.locationModel.LocationCoordinatesDTO;
import project.model.locationModel.LocationResponse;
import project.service.LocationService;

import java.util.List;
@Tag(name = "Locations")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }
    @Operation(summary = "Get location coordinates for showing locations on map")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/locationCoordinates")
    List<LocationCoordinatesDTO> getLocationCoordinates(){
        return locationService.getLocationCoordinates();
    }
    @Operation(summary = "Get location by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/location/{id}")
    ResponseEntity<LocationResponse> getLocation(@PathVariable("id")Long id){
        LocationResponse locationResponse = locationService.getLocationResponseById(id);
        if(locationResponse != null) {
            return new ResponseEntity<>(locationResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Get locations list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/locationsList")
    Page<LocationAddressDTO> getLocationAddresses(PageableDTO pageableDTO){
        int page = 0;
        int size = 5;
        if(pageableDTO.getPage() >= 0){
            page = pageableDTO.getPage();
        }
        if(pageableDTO.getSize() > 0){
            size = pageableDTO.getSize();
        }
        Pageable pageable;
        if(pageableDTO.getSortField() != null){
            Sort sort = Sort.by("id").ascending();
            if(pageableDTO.getSortDirection() != null){
                if(pageableDTO.getSortDirection().equals("ASC")){
                    sort = Sort.by(pageableDTO.getSortField()).ascending();
                }
                if(pageableDTO.getSortDirection().equals("DESC")){
                    sort = Sort.by(pageableDTO.getSortField()).descending();
                }

            }
            pageable = PageRequest.of(page,size,sort);
        } else {
            pageable = PageRequest.of(page,size,Sort.by("id").ascending());
        }
        return locationService.getLocationAddresses(pageable);
    }


}
