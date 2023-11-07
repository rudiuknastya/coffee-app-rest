package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.ShoppingCart;
import project.repository.ShoppingCartRepository;
import project.service.ShoppingCartService;
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
}
