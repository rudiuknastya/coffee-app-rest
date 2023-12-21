package project.serviceImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import project.entity.Product;
import project.entity.ShoppingCart;
import project.entity.ShoppingCartItem;
import project.mapper.ProductMapper;
import project.model.PageableDTO;
import project.model.productModel.AwardDTO;
import project.repository.ProductRepository;
import project.repository.ShoppingCartItemRepository;
import project.repository.ShoppingCartRepository;
import project.repository.UserRepository;
import project.service.AwardService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AwardServiceImpl implements AwardService {
    private final ProductRepository productRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public AwardServiceImpl(ProductRepository productRepository, ShoppingCartItemRepository shoppingCartItemRepository, ShoppingCartRepository shoppingCartRepository) {
        this.productRepository = productRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");

    @Override
    public Page<AwardDTO> getAwards(String email, PageableDTO pageableDTO) {
        logger.info("getAwards() - Finding user products for award dto for user with email "+email);
        Pageable pageable;
        Sort sort;
        if(pageableDTO.getSortDirection().equals("DESC")){
            sort = Sort.by(pageableDTO.getSortField()).descending();
        }
        else{
            sort = Sort.by(pageableDTO.getSortField()).ascending();
        }
        pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getSize(),sort);
        Page<Product> products = productRepository.findUserProducts(email,pageable);
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        List<AwardDTO> awardDTOS = ProductMapper.PRODUCT_MAPPER.productListToAwardDTOList(products.getContent());
//        for(AwardDTO award : awardDTOS){
//            award.setImage(baseUrl+"/uploads/"+award.getImage());
//        }
        Page<AwardDTO> awardDTOPage = new PageImpl<>(awardDTOS,pageable, products.getTotalElements());
        logger.info("getAwards() - User products for award dto were found");
        return awardDTOPage;
    }

    @Override
    public void addAwardToShoppingCart(String email, Long id) {
        logger.info("addAwardToShoppingCart() - Adding award to shopping cart");
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email);
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setQuantity(1L);
        shoppingCartItem.setPrice(BigDecimal.valueOf(0));
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        shoppingCartItem.setProduct(product);
        shoppingCartItem.setShoppingCart(shoppingCart);
        shoppingCartItemRepository.save(shoppingCartItem);
        logger.info("addAwardToShoppingCart() - Award was added to shopping cart");
    }
}
