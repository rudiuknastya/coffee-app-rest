package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.PageableDTO;
import project.model.productModel.AwardDTO;

public interface AwardService {
    Page<AwardDTO> getAwards(String email, PageableDTO pageableDTO);
    void addAwardToShoppingCart(String email, Long id);
}
