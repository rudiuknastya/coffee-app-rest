package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.ShoppingCartItem;
import project.mapper.ShoppingCartMapper;
import project.model.shoppingCartModel.ShoppingCartResponse;
import project.repository.ShoppingCartItemRepository;
import project.service.ShoppingCartItemService;

import java.util.List;

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
    @Override
    public ShoppingCartResponse getShoppingCartResponse(String email) {
        logger.info("getShoppingCartResponse() - Finding shopping cart items for shopping cart response");
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemRepository.findShoppingCartItemsWithAdditives(email);
        ShoppingCartResponse shoppingCartResponse = ShoppingCartMapper.shoppingCartItemsToShoppingCartResponse(shoppingCartItems);
        logger.info("getShoppingCartResponse() - Shopping cart items were found");
        return shoppingCartResponse;
    }

    @Override
    public ShoppingCartItem getShoppingCartItemWithAdditivesById(Long id) {
        logger.info("getShoppingCartItemById() - Finding shopping cart item by id "+id);
        ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findShoppingCartItemWithAdditivesById(id);
        logger.info("getShoppingCartItemById() - Shopping cart item was found");
        return shoppingCartItem;
    }

    @Override
    public void deleteShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        logger.info("deleteShoppingCartItem() - Deleting shopping cart item");
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
}
