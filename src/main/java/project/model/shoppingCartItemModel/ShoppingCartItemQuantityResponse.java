package project.model.shoppingCartItemModel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class ShoppingCartItemQuantityResponse {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "2", required = true)
    private Long quantity;
    @Schema(example = "45", required = true)
    private BigDecimal price;
    @Schema(example = "90", required = true)
    private BigDecimal shoppingCartPrice;

    public ShoppingCartItemQuantityResponse() {
    }

    public ShoppingCartItemQuantityResponse(Long id, Long quantity, BigDecimal price, BigDecimal shoppingCartPrice) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.shoppingCartPrice = shoppingCartPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getShoppingCartPrice() {
        return shoppingCartPrice;
    }

    public void setShoppingCartPrice(BigDecimal shoppingCartPrice) {
        this.shoppingCartPrice = shoppingCartPrice;
    }
}
