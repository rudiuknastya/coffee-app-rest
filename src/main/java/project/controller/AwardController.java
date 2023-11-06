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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.model.PageableDTO;
import project.model.productModel.AwardDTO;
import project.model.productModel.ProductResponse;
import project.service.AwardService;

@RestController
@Tag(name = "Award")
@SecurityRequirement(name = "Bearer Authentication")
public class AwardController {
    private final AwardService awardService;

    public AwardController(AwardService awardService) {
        this.awardService = awardService;
    }
    @Operation(summary = "Get user awards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/awards")
    Page<AwardDTO> getUserAwards(PageableDTO pageableDTO){
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        return awardService.getAwards(email,pageable);
    }
}
