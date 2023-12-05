package project.model.shoppingCartModel;

import io.swagger.v3.oas.annotations.media.Schema;
import project.model.shoppingCartItemModel.ShoppingCartItemResponse;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartResponse {
    @Schema(example = "1", required = true)
    private Long id;
    private List<ShoppingCartItemResponse> shoppingCartItems;
    @Schema(example = "55", required = true)
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ShoppingCartItemResponse> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(List<ShoppingCartItemResponse> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
