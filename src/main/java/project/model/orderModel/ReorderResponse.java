package project.model.orderModel;

import io.swagger.v3.oas.annotations.media.Schema;
import project.model.orderItemModel.OrderItemResponse;

import java.math.BigDecimal;
import java.util.List;

public class ReorderResponse {
    @Schema(example = "1", required = true)
    private Long orderId;
    private List<OrderItemResponse> orderItemResponses;
    @Schema(example = "55", required = true)
    private BigDecimal price;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<OrderItemResponse> getOrderItemResponses() {
        return orderItemResponses;
    }

    public void setOrderItemResponses(List<OrderItemResponse> orderItemResponses) {
        this.orderItemResponses = orderItemResponses;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
