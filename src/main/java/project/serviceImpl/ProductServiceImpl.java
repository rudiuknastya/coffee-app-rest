package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Product;
import project.mapper.ProductMapper;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductResponse;
import project.repository.ProductRepository;
import project.service.ProductService;
import static project.specification.ProductSpecification.*;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    String uploadPath = "C:\\Users\\Anastassia\\IdeaProjects\\Coffee-app-admin\\uploads";
    @Override
    public Page<ProductResponse> getProductsForCategory(Long categoryId, Pageable pageable) {
        logger.info("getProductsForCategory() - Finding products for product responses for page "+pageable.getPageNumber()+" and category id "+categoryId);
        Page<Product> products = productRepository.findAll(byCategoryId(categoryId).and(byDeleted()).and(byStatus()), pageable);
        List<ProductResponse> productResponses = ProductMapper.PRODUCT_MAPPER.productListToProductResponseList(products.getContent());
        for(ProductResponse productResponse: productResponses){
            String s = "https://slj.avada-media-dev1.od.ua/Coffee_App_A_Rudiuk/uploads/";
            productResponse.setImage(s+productResponse.getImage());
        }
        Page<ProductResponse> productResponsePage = new PageImpl<>(productResponses,pageable,products.getTotalElements());
        logger.info("getProductsForCategory() - Products for product responses were found");
        return productResponsePage;
    }

    @Override
    public ProductDTO getProductDTOById(Long id) {
        logger.info("getProductDTOById() - Finding product for product dto by id "+id);
        Product product = productRepository.findProductWithAdditiveTypesById(id);
        ProductDTO productDTO = ProductMapper.productToProductDTO(product);
        String s = "https://slj.avada-media-dev1.od.ua/Coffee_App_A_Rudiuk/uploads/";
        productDTO.setImage(s+productDTO.getImage());
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
