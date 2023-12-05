package project.model.shoppingCartModel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class ShoppingCartPriceResponse {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "130", required = true)
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
