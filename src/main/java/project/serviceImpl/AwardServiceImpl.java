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
import project.entity.User;
import project.mapper.ProductMapper;
import project.mapper.ShoppingCartItemMapper;
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
    private final UserRepository userRepository;

    public AwardServiceImpl(ProductRepository productRepository, ShoppingCartItemRepository shoppingCartItemRepository, ShoppingCartRepository shoppingCartRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
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
        List<AwardDTO> awardDTOS = ProductMapper.PRODUCT_MAPPER.productListToAwardDTOList(products.getContent());
        Page<AwardDTO> awardDTOPage = new PageImpl<>(awardDTOS,pageable, products.getTotalElements());
        logger.info("getAwards() - User products for award dto were found");
        return awardDTOPage;
    }

    @Override
    public void addAwardToShoppingCart(String email, Long id) {
        logger.info("addAwardToShoppingCart() - Adding award to shopping cart");
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email);
        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Product for award was not found by id "+id));
        ShoppingCartItem shoppingCartItem = ShoppingCartItemMapper.SHOPPING_CART_ITEM_MAPPER.createShoppingCart(product,shoppingCart,BigDecimal.valueOf(0));
        shoppingCartItemRepository.save(shoppingCartItem);
        User user = userRepository.findWithProductsByEmail(email).orElseThrow(()-> new EntityNotFoundException("User was not found by email "+email));
        user.getProducts().remove(product);
        userRepository.save(user);
        logger.info("addAwardToShoppingCart() - Award was added to shopping cart");
    }
}
