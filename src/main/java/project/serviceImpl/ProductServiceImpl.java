package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import project.entity.Product;
import project.mapper.ProductMapper;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductResponse;
import project.repository.ProductRepository;
import project.service.ProductService;

import java.util.List;

import static project.specification.ProductSpecification.*;
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<ProductResponse> getProductsForCategory(Long categoryId, Pageable pageable) {
        logger.info("getProductsForCategory() - Finding products for product responses for page "+pageable.getPageNumber()+" and category id "+categoryId);
        Page<Product> products = productRepository.findAll(byCategoryId(categoryId).and(byDeleted()).and(byStatus()), pageable);
        List<ProductResponse> productResponses = ProductMapper.PRODUCT_MAPPER.productListToProductResponseList(products.getContent());
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        for(ProductResponse productResponse: productResponses){
            productResponse.setImage(baseUrl+"/uploads/"+productResponse.getImage());
        }
        Page<ProductResponse> productResponsePage = new PageImpl<>(productResponses,pageable,products.getTotalElements());
        logger.info("getProductsForCategory() - Products for product responses were found");
        return productResponsePage;
    }

    @Override
    public ProductDTO getProductDTOById(Long id) {
        logger.info("getProductDTOById() - Finding product for product dto by id "+id);
        Product product = productRepository.findProductWithAdditiveTypesById(id).orElseThrow(EntityNotFoundException::new);
        ProductDTO productDTO = ProductMapper.productToProductDTO(product);
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        productDTO.setImage(baseUrl+"/uploads/"+productDTO.getImage());
        logger.info("getProductDTOById() - Product for product dto was found");
        return productDTO;
    }

    @Override
    public Product getProductById(Long id) {
        logger.info("getProductById() - Finding product by id "+id);
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getProductById() - Product was found");
        return product;
    }
}
