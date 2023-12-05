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
        Pageable pageable;
        Sort sort;
        if(pageableDTO.getSortDirection().equals("DESC")){
            sort = Sort.by(pageableDTO.getSortField()).descending();
        }
         else{
            sort = Sort.by(pageableDTO.getSortField()).ascending();
        }
        pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getSize(),sort);
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
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productService.getProductDTOById(id), HttpStatus.OK);
    }

}
