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
        if(page >= 0) {
            this.page = page;
        } else {
            this.page = 0;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if(size >= 1) {
            this.size = size;
        } else {
            this.size = 5;
        }
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        if(sortField != null && !sortField.equals("")) {
            this.sortField = sortField;
        } else {
            this.sortField = "id";
        }
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        if(sortDirection != null && !sortDirection.equals("")) {
            this.sortDirection = sortDirection;
        } else {
            this.sortDirection = "ASC";
        }
    }
}
