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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.model.PageableDTO;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductResponse;
import project.service.ProductService;

@Tag(name = "Product")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @Operation(summary = "Get products for category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/products/{categoryId}")
    Page<ProductResponse> getProductsForCategory(@PathVariable("categoryId")Long categoryId, PageableDTO pageableDTO){
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
        return productService.getProductsForCategory(categoryId,pageable);
    }

    @Operation(summary = "Get product with additive types by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/product/{id}")
    ResponseEntity<ProductDTO> getProduct(@PathVariable("id")Long id){
        ProductDTO productDTO = productService.getProductDTOById(id);
        if(productDTO != null){
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
