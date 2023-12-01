package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.entity.Additive;
import project.model.additiveModel.AdditiveDTO;

import java.util.List;
@Mapper(componentModel = "spring")
public interface AdditiveMapper {
    AdditiveMapper ADDITIVE_MAPPER = Mappers.getMapper(AdditiveMapper.class);
    List<AdditiveDTO> additiveListToAdditiveDTOList(List<Additive> additives);
}
