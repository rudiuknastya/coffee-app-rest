package project.model.additiveModel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class AdditiveDTO {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "Без сиропу", required = true)
    private String name;
    @Schema(example = "10", required = true)
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
