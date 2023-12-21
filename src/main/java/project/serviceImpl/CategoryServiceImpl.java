package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import project.entity.Category;
import project.mapper.CategoryMapper;
import project.model.PageableDTO;
import project.model.categoryModel.CategoryResponse;
import project.repository.CategoryRepository;
import project.service.CategoryService;
import static project.specification.CategorySpecification.*;
import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    private Logger logger = LogManager.getLogger("serviceLogger");
    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Page<CategoryResponse> getCategoryResponses(PageableDTO pageableDTO) {
        logger.info("getCategoryResponses() - Finding categories for category responses");
        Pageable pageable;
        Sort sort;
        if(pageableDTO.getSortDirection().equals("DESC")){
            sort = Sort.by(pageableDTO.getSortField()).descending();
        }
        else{
            sort = Sort.by(pageableDTO.getSortField()).ascending();
        }
        pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getSize(),sort);
        Page<Category> categories = categoryRepository.findAll(byDeleted().and(byStatus()), pageable);
        List<CategoryResponse> categoryResponses = CategoryMapper.CATEGORY_MAPPER.categoryListToCategoryResponseList(categories.getContent());
        Page<CategoryResponse> categoryResponsePage = new PageImpl<>(categoryResponses,pageable,categories.getTotalElements());
        logger.info("getCategoryResponses() - Categories for category responses were found");
        return categoryResponsePage;
    }
}
