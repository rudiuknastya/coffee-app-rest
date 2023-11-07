package project.service;

import project.entity.Additive;
import project.model.additiveModel.AdditiveDTO;

import java.util.List;

public interface AdditiveService {
    List<AdditiveDTO> getAdditiveDTOsByAdditiveTypeId(Long additiveTypeId);
    List<Additive> getAdditivesById(List<Long> ids);
}
