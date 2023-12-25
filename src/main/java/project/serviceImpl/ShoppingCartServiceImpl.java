package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.Location;
import project.entity.ShoppingCart;
import project.entity.ShoppingCartItem;
import project.mapper.ShoppingCartMapper;
import project.model.shoppingCartModel.ShoppingCartPriceResponse;
import project.model.shoppingCartModel.ShoppingCartResponse;
import project.repository.ShoppingCartItemRepository;
import project.repository.ShoppingCartRepository;
import project.service.ShoppingCartService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, ShoppingCartItemRepository shoppingCartItemRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
        logger.info("saveShoppingCart() - Saving shopping cart");
        ShoppingCart shoppingCart1 = shoppingCartRepository.save(shoppingCart);
        logger.info("saveShoppingCart() - Shopping cart was saved");
        return shoppingCart1;
    }

    @Override
    public ShoppingCartPriceResponse getShoppingCartPrice(String email) {
        logger.info("getShoppingCartPrice() - Finding shopping cart for shopping cart price response by user email "+email);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email);
        ShoppingCartPriceResponse shoppingCartPriceResponse = ShoppingCartMapper.SHOPPING_CART_MAPPER.shoppingCartToShoppingCartPriceResponse(shoppingCart);
        logger.info("getShoppingCartPrice() - Shopping cart for shopping cart price response was found");
        return shoppingCartPriceResponse;
    }

    @Override
    public void resetShoppingCart(String email) {
        logger.info("resetShoppingCart() - Resetting shopping cart");
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email);
        shoppingCart.setPrice(BigDecimal.valueOf(0));
        shoppingCart.setLocation(null);
        shoppingCartRepository.save(shoppingCart);
        logger.info("resetShoppingCart() - Shopping cart was reset");

    }

    @Override
    public void setShoppingCartLocation(Location location, String email) {
        logger.info("setShoppingCartLocation() - Setting shopping cart location");
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email);
        shoppingCart.setLocation(location);
        shoppingCartRepository.save(shoppingCart);
        logger.info("setShoppingCartLocation() - Shopping cart location was set");
    }

    @Override
    public ShoppingCartResponse getShoppingCartResponse(String email) {
        logger.info("getShoppingCartResponse() - Finding shopping cart items for shopping cart response");
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email);
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemRepository.findShoppingCartItemsWithAdditives(email);
        ShoppingCartResponse shoppingCartResponse = ShoppingCartMapper.SHOPPING_CART_MAPPER.shoppingCartItemsToShoppingCartResponse(shoppingCartItems, shoppingCart);
        logger.info("getShoppingCartResponse() - Shopping cart items were found");
        return shoppingCartResponse;
    }
}
