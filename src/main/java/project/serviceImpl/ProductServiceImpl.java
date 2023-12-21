package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import project.entity.Product;
import project.mapper.ProductMapper;
import project.model.PageableDTO;
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
    public Page<ProductResponse> getProductsForCategory(Long categoryId, PageableDTO pageableDTO) {
        logger.info("getProductsForCategory() - Finding products for product responses for page "+pageableDTO.getPage()+" and category id "+categoryId);
        Pageable pageable;
        Sort sort;
        if(pageableDTO.getSortDirection().equals("DESC")){
            sort = Sort.by(pageableDTO.getSortField()).descending();
        }
        else{
            sort = Sort.by(pageableDTO.getSortField()).ascending();
        }
        pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getSize(),sort);
        Page<Product> products = productRepository.findAll(byCategoryId(categoryId).and(byDeleted()).and(byStatus()), pageable);
        List<ProductResponse> productResponses = ProductMapper.PRODUCT_MAPPER.productListToProductResponseList(products.getContent());
        Page<ProductResponse> productResponsePage = new PageImpl<>(productResponses,pageable,products.getTotalElements());
        logger.info("getProductsForCategory() - Products for product responses were found");
        return productResponsePage;
    }

    @Override
    public ProductDTO getProductDTOById(Long id) {
        logger.info("getProductDTOById() - Finding product for product dto by id "+id);
        Product product = productRepository.findProductWithAdditiveTypesById(id).orElseThrow(()-> new EntityNotFoundException("Product was not found by id "+id));
        ProductDTO productDTO = ProductMapper.PRODUCT_MAPPER.productToProductDTO(product);
        logger.info("getProductDTOById() - Product for product dto was found");
        return productDTO;
    }

}
