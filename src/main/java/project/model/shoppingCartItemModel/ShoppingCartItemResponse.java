package project.model.shoppingCartItemModel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartItemResponse {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "Лате", required = true)
    private String productName;
    @Schema(example = "2", required = true)
    private Long quantity;
    @Schema(example = "45", required = true)
    private BigDecimal price;
    private List<String> additives;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public List<String> getAdditives() {
        return additives;
    }

    public void setAdditives(List<String> additives) {
        this.additives = additives;
    }
}
