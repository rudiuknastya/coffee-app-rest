package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.entity.Additive;
import project.entity.AdditiveType;
import project.model.additiveTypeModel.AdditiveTypeDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdditiveTypeMapper {
    AdditiveTypeMapper ADDITIVE_TYPE_MAPPER = Mappers.getMapper(AdditiveTypeMapper.class);
    List<AdditiveTypeDTO> additiveToAdditiveTypeDTO(List<AdditiveType> additiveTypes);
}
