package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.AdditiveType;
import project.entity.Product;
import project.model.additiveTypeModel.AdditiveTypeDTO;
import project.model.productModel.ProductDTO;
import project.model.productModel.ProductResponse;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper PRODUCT_MAPPER = Mappers.getMapper(ProductMapper.class);
    List<ProductResponse> productListToProductResponseList(List<Product> products);
    @Named("productToProductDTO")
    static ProductDTO productToProductDTO(Product product){
        if(product == null){
            return null;
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImage(product.getImage());
        if(product.getAdditiveTypes() != null){
            List<AdditiveTypeDTO> additiveTypeDTOS = new ArrayList<>(product.getAdditiveTypes().size());
            for(AdditiveType additiveType: product.getAdditiveTypes()){
                AdditiveTypeDTO additiveTypeDTO = new AdditiveTypeDTO();
                additiveTypeDTO.setId(additiveType.getId());
                additiveTypeDTO.setName(additiveType.getName());
                additiveTypeDTOS.add(additiveTypeDTO);
            }
            productDTO.setAdditiveTypes(additiveTypeDTOS);
        }
        return productDTO;
    }
}
