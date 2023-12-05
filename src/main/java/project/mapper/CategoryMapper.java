package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.entity.Category;
import project.model.categoryModel.CategoryResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper CATEGORY_MAPPER = Mappers.getMapper(CategoryMapper.class);
    List<CategoryResponse> categoryListToCategoryResponseList(List<Category> categories);
}
