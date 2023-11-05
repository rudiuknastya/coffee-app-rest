package project.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class PageableDTO {
    @Schema(example = "0", required = true)
    private int page;
    @Schema(example = "3", required = true)
    private int size;
    @Schema(example = "id", required = true)
    private String sortField;
    @Schema(example = "ASC", required = true)
    private String sortDirection;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
