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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.entity.ShoppingCart;
import project.model.PageableDTO;
import project.model.productModel.AwardDTO;
import project.model.productModel.ProductResponse;
import project.service.AwardService;
import project.service.ShoppingCartService;

@RestController
@Tag(name = "Award")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping( "/api/v1")
public class AwardController {
    private final AwardService awardService;

    public AwardController(AwardService awardService) {
        this.awardService = awardService;
    }

    @Operation(summary = "Get user awards", description = "Get user's awards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",schema = @Schema(implementation = AwardDTO.class))}),
            @ApiResponse(responseCode = "401", description = "User unauthorized", content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @GetMapping("/awards")
    ResponseEntity<?> getUserAwards(PageableDTO pageableDTO){
        Pageable pageable;
        Sort sort;
        if(pageableDTO.getSortDirection().equals("DESC")){
            sort = Sort.by(pageableDTO.getSortField()).descending();
        }
        else{
            sort = Sort.by(pageableDTO.getSortField()).ascending();
        }
        pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getSize(),sort);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Page<AwardDTO> awardDTOS = awardService.getAwards(email,pageable);
        return new ResponseEntity<>(awardDTOS, HttpStatus.OK);
    }
    @Operation(summary = "Add award to shopping cart", description = "Adding award to shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Award not found",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @PostMapping("/awards/add/{id}")
    ResponseEntity<?> addAwardToShoppingCart(@PathVariable
                                             @Parameter(name = "id", description = "Award id", example = "1")
                                             Long id){
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        awardService.addAwardToShoppingCart(email,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
