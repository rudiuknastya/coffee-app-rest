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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.model.PageableDTO;
import project.model.categoryModel.CategoryResponse;
import project.service.CategoryService;

@Tag(name = "Category")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @Operation(summary = "Get categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/categories")
    Page<CategoryResponse> getCategories(PageableDTO pageableDTO){
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
        return categoryService.getCategoryResponses(pageable);
    }
}
