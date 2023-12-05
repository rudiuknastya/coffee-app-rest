package project.model.productModel;

import io.swagger.v3.oas.annotations.media.Schema;
import project.model.additiveTypeModel.AdditiveTypeDTO;

import java.util.List;

public class ProductDTO {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "Лате", required = true)
    private String name;
    @Schema(example = "Лате — кавовий напій родом з Італії, що складається з молока (італ. latte) і кави еспресо.", required = true)
    private String description;
    @Schema(example = "https://slj.avada-media-dev1.od.ua/Coffee_App_A_Rudiuk/uploads/9a36b1d0-ccd8-4496-bdd1-fa342dd93fff.n2a7n5rq8qpuvt918lescg84q89hinew.jpg", required = true)
    private String image;
    private List<AdditiveTypeDTO> additiveTypes;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<AdditiveTypeDTO> getAdditiveTypes() {
        return additiveTypes;
    }

    public void setAdditiveTypes(List<AdditiveTypeDTO> additiveTypes) {
        this.additiveTypes = additiveTypes;
    }
}
