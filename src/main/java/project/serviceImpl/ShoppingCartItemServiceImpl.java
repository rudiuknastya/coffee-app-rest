package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.entity.Additive;
import project.entity.Product;
import project.entity.ShoppingCartItem;
import project.entity.User;
import project.mapper.ShoppingCartMapper;
import project.model.shoppingCartItemModel.ShoppingCartItemRequest;
import project.model.shoppingCartModel.ShoppingCartResponse;
import project.repository.AdditiveRepository;
import project.repository.ProductRepository;
import project.repository.ShoppingCartItemRepository;
import project.repository.UserRepository;
import project.service.ShoppingCartItemService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShoppingCartItemServiceImpl implements ShoppingCartItemService {
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ProductRepository productRepository;
    private final AdditiveRepository additiveRepository;
    private final UserRepository userRepository;

    public ShoppingCartItemServiceImpl(ShoppingCartItemRepository shoppingCartItemRepository, ProductRepository productRepository, AdditiveRepository additiveRepository, UserRepository userRepository) {
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.productRepository = productRepository;
        this.additiveRepository = additiveRepository;
        this.userRepository = userRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public ShoppingCartItem saveShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        logger.info("saveShoppingCartItem() - Saving shopping cart item");
        ShoppingCartItem shoppingCartItem1 = shoppingCartItemRepository.save(shoppingCartItem);
        logger.info("saveShoppingCartItem() - Shopping cart item was saved");
        return shoppingCartItem1;
    }

    @Override
    public ShoppingCartItem getShoppingCartItemWithAdditivesById(Long id) {
        logger.info("getShoppingCartItemById() - Finding shopping cart item by id "+id);
        ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findShoppingCartItemWithAdditivesById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getShoppingCartItemById() - Shopping cart item was found");
        return shoppingCartItem;
    }

    @Override
    public void deleteShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        logger.info("deleteShoppingCartItem() - Deleting shopping cart item");
        if(shoppingCartItem.getPrice().compareTo(BigDecimal.valueOf(0)) == 0 ){
            User user = userRepository.findWithProductsById(shoppingCartItem.getShoppingCart().getUser().getId()).orElseThrow(()-> new EntityNotFoundException("User not found by id "+shoppingCartItem.getShoppingCart().getUser().getId()));
            user.getProducts().add(shoppingCartItem.getProduct());
            userRepository.save(user);
        }
        shoppingCartItemRepository.delete(shoppingCartItem);
        logger.info("deleteShoppingCartItem() - Shopping cart item was deleted");
    }

    @Override
    public ShoppingCartItem getShoppingCartItemById(Long id) {
        logger.info("getShoppingCartItemById() - Finding shopping cart item by id "+id);
        ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getShoppingCartItemById() - Shopping cart item was found");
        return shoppingCartItem;
    }

    @Override
    public void deleteShoppingCartItemsByUserEmail(String email) {
        logger.info("deleteUserShoppingCartItems() - Deleting shopping cart items by email "+email);
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemRepository.findShoppingCartItemsWithAdditives(email);
        shoppingCartItemRepository.deleteAll(shoppingCartItems);
        logger.info("deleteUserShoppingCartItems() - Shopping cart items were deleted");
    }

    @Override
    public List<ShoppingCartItem> getShoppingCartItemsWithAdditives(String email) {
        logger.info("getShoppingCartItemsWithAdditives() - Finding shopping cart items with additives by email "+email);
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemRepository.findShoppingCartItemsWithAdditives(email);
        logger.info("getShoppingCartItemsWithAdditives() - Shopping cart items were found");
        return shoppingCartItems;
    }

    @Override
    public void deleteShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
        logger.info("deleteShoppingCartItems() - Deleting shopping cart items");
        shoppingCartItemRepository.deleteAll(shoppingCartItems);
        logger.info("deleteShoppingCartItems() - Shopping cart items were deleted");
    }

    @Override
    public ShoppingCartItem updateShoppingCartItem(Long id, Long quantity) {
        logger.info("updateShoppingCartItem() - Updating shopping cart item");
        ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        BigDecimal p = shoppingCartItem.getPrice();
        p = p.divide(BigDecimal.valueOf(shoppingCartItem.getQuantity()));
        p = p.multiply(BigDecimal.valueOf(quantity));

        BigDecimal op = shoppingCartItem.getShoppingCart().getPrice();
        op = op.subtract(shoppingCartItem.getPrice());
        shoppingCartItem.setPrice(p);
        op = op.add(shoppingCartItem.getPrice());
        shoppingCartItem.getShoppingCart().setPrice(op);
        shoppingCartItem.setQuantity(quantity);
        ShoppingCartItem shoppingCartItem1 = shoppingCartItemRepository.save(shoppingCartItem);
        logger.info("updateShoppingCartItem() - Shopping cart item was updated");
        return shoppingCartItem1;
    }

    @Override
    public void createShoppingCartItem(Long productId, ShoppingCartItemRequest shoppingCartItemRequest) {
        logger.info("createShoppingCartItem() - Creating shopping cart item");
        BigDecimal price = new BigDecimal(0);
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setQuantity(shoppingCartItemRequest.getQuantity());
        Product product = productRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
        price = price.add(product.getPrice());
        shoppingCartItem.setProduct(product);
        List<Additive> additives = additiveRepository.findAllById(shoppingCartItemRequest.getAdditiveIds());
        for(Additive additive: additives){
            price = price.add(additive.getPrice());
        }
        price = price.multiply(BigDecimal.valueOf(shoppingCartItemRequest.getQuantity()));
        shoppingCartItem.setPrice(price);
        shoppingCartItem.setAdditives(additives);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findWithShoppingCartByEmail(email);
        user.getShoppingCart().setPrice(user.getShoppingCart().getPrice().add(price));
        shoppingCartItem.setShoppingCart(user.getShoppingCart());
        shoppingCartItemRepository.save(shoppingCartItem);
        logger.info("createShoppingCartItem() - Shopping cart item was created");
    }
}
