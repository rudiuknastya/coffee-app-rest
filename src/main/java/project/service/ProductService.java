package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductResponse;

import java.util.List;

public interface ProductService {
    Page<ProductResponse> getProductsForCategory(Long categoryId, Pageable pageable);
    ProductDTO getProductDTOById(Long id);
}
