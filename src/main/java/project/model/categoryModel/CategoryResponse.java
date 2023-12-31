package project.model.categoryModel;

import io.swagger.v3.oas.annotations.media.Schema;

public class CategoryResponse {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "Кава", required = true)
    private String name;

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
}
