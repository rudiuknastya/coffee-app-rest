package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.ShoppingCartItem;
import project.repository.ShoppingCartItemRepository;
import project.service.ShoppingCartItemService;
@Service
public class ShoppingCartItemServiceImpl implements ShoppingCartItemService {
    private final ShoppingCartItemRepository shoppingCartItemRepository;

    public ShoppingCartItemServiceImpl(ShoppingCartItemRepository shoppingCartItemRepository) {
        this.shoppingCartItemRepository = shoppingCartItemRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public ShoppingCartItem saveShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        logger.info("saveShoppingCartItem() - Saving shopping cart item");
        ShoppingCartItem shoppingCartItem1 = shoppingCartItemRepository.save(shoppingCartItem);
        logger.info("saveShoppingCartItem() - Shopping cart item was saved");
        return shoppingCartItem1;
    }
}
