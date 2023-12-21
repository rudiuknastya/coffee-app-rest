package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.PageableDTO;
import project.model.categoryModel.CategoryResponse;

import java.util.List;


public interface CategoryService {
    Page<CategoryResponse> getCategoryResponses(PageableDTO pageableDTO);
}
