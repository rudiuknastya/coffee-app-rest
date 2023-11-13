package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.ShoppingCart;
import project.mapper.ShoppingCartMapper;
import project.model.shoppingCartModel.ShoppingCartPriceResponse;
import project.repository.ShoppingCartRepository;
import project.service.ShoppingCartService;

import java.math.BigDecimal;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
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
    public void deleteShoppingCart(ShoppingCart shoppingCart) {
        logger.info("deleteShoppingCart() - Deleting shopping cart");
        shoppingCartRepository.delete(shoppingCart);
        logger.info("deleteShoppingCart() - Shopping cart was deleted");
    }
}
