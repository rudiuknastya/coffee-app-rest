package project.model.shoppingCartItemModel;

import java.util.List;

public class ShoppingCartItemRequest {
    private List<Long> additiveIds;
    private Long quantity;

    public List<Long> getAdditiveIds() {
        return additiveIds;
    }

    public void setAdditiveIds(List<Long> additiveIds) {
        this.additiveIds = additiveIds;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
