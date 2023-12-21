package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.Product;
import project.model.PageableDTO;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductResponse;

import java.util.List;

public interface ProductService {
    Page<ProductResponse> getProductsForCategory(Long categoryId, PageableDTO pageableDTO);
    ProductDTO getProductDTOById(Long id);

}
