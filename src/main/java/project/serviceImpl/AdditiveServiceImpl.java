package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.Additive;
import project.mapper.AdditiveMapper;
import project.model.additiveModel.AdditiveDTO;
import project.repository.AdditiveRepository;
import project.service.AdditiveService;
import static project.specification.AdditiveSpecification.*;
import java.util.List;
@Service
public class AdditiveServiceImpl implements AdditiveService {
    private final AdditiveRepository additiveRepository;

    public AdditiveServiceImpl(AdditiveRepository additiveRepository) {
        this.additiveRepository = additiveRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");

    @Override
    public List<AdditiveDTO> getAdditiveDTOsByAdditiveTypeId(Long additiveTypeId) {
        logger.info("getAdditiveDTOsByAdditiveTypeId() - Finding additives for additive dto for additive type id "+additiveTypeId);
        List<Additive> additives = additiveRepository.findAll(byDeleted().and(byStatus()).and(byAdditiveTypeId(additiveTypeId)));
        List<AdditiveDTO> additiveDTOS = AdditiveMapper.ADDITIVE_MAPPER.additiveListToAdditiveDTOList(additives);
        logger.info("getAdditiveDTOsByAdditiveTypeId() - Additives for additive dto were found");
        return additiveDTOS;
    }

    @Override
    public List<Additive> getAdditivesById(List<Long> ids) {
        logger.info("getAdditivesById() - Finding additives by ids");
        List<Additive> additives = additiveRepository.findAllById(ids);
        logger.info("getAdditivesById() - Additives were found");
        return additives;
    }
}
