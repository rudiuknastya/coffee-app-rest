package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.productModel.AwardDTO;

public interface AwardService {
    Page<AwardDTO> getAwards(String email, Pageable pageable);
}
