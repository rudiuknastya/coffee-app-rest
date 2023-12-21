package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import project.entity.AdditiveType;
import project.entity.Product;
import project.model.additiveTypeModel.AdditiveTypeDTO;
import project.model.productModel.AwardDTO;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductResponse;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper PRODUCT_MAPPER = Mappers.getMapper(ProductMapper.class);
    List<ProductResponse> productListToProductResponseList(List<Product> products);
    List<AwardDTO> productListToAwardDTOList(List<Product> products);
    @Mapping(target = "image", expression = "java(createImageUrl(product))")
    AwardDTO productToAwardDTO(Product product);
    default String createImageUrl(Product product){
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return baseUrl+"/uploads/"+product.getImage();
    }
    ProductDTO productToProductDTO(Product product);
    default List<AdditiveTypeDTO> createAdditiveDTOList(Product product){
        if(product.getAdditiveTypes() != null){
            return AdditiveTypeMapper.ADDITIVE_TYPE_MAPPER.additiveToAdditiveTypeDTO(product.getAdditiveTypes());
        } else {
            return new ArrayList<>();
        }
    }
}
