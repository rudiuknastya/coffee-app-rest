package project.model.orderItemModel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public class OrderItemResponse {
    @Schema(example = "Кава", required = true)
    private String productName;
    @Schema(example = "30", required = true)
    private BigDecimal price;
    @Schema(example = "2", required = true)
    private Long quantity;
    private List<String> additives;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public List<String> getAdditives() {
        return additives;
    }

    public void setAdditives(List<String> additives) {
        this.additives = additives;
    }
}
