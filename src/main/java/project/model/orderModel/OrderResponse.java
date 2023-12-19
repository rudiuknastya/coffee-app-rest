package project.model.orderModel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderResponse {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "2023-01-14", required = true)
    private LocalDate orderDate;
    @Schema(example = "100", required = true)
    private BigDecimal price;
    @Schema(example = "Київ", required = true)
    private String locationCity;
    @Schema(example = "вул.Хрещатик 1", required = true)
    private String locationAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
}
