package project.model.productModel;

import project.model.additiveTypeModel.AdditiveTypeDTO;

import java.util.List;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
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
