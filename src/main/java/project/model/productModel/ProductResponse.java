package project.model.productModel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class ProductResponse {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "Лате", required = true)
    private String name;
    @Schema(example = "60", required = true)
    private BigDecimal price;
    @Schema(example = "https://slj.avada-media-dev1.od.ua/Coffee_App_A_Rudiuk/uploads/9a36b1d0-ccd8-4496-bdd1-fa342dd93fff.n2a7n5rq8qpuvt918lescg84q89hinew.jpg", required = true)
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
