package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Category;
import project.mapper.CategoryMapper;
import project.model.categoryModel.CategoryResponse;
import project.repository.CategoryRepository;
import project.service.CategoryService;
import static project.specification.CategorySpecification.*;
import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<CategoryResponse> getCategoryResponses(Pageable pageable) {
        logger.info("getCategoryResponses() - Finding categories for category responses for page "+pageable.getPageNumber());
        Page<Category> categories = categoryRepository.findAll(byDeleted().and(byStatus()), pageable);
        List<CategoryResponse> categoryResponses = CategoryMapper.CATEGORY_MAPPER.categoryListToCategoryResponseList(categories.getContent());
        Page<CategoryResponse> categoryResponsePage = new PageImpl<>(categoryResponses,pageable,categories.getTotalElements());
        logger.info("getCategoryResponses() - Categories for category responses were found");
        return categoryResponsePage;
    }
}
