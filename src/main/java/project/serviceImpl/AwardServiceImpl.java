package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Product;
import project.mapper.ProductMapper;
import project.model.productModel.AwardDTO;
import project.repository.ProductRepository;
import project.repository.UserRepository;
import project.service.AwardService;

import java.util.List;

@Service
public class AwardServiceImpl implements AwardService {
    private final ProductRepository productRepository;

    public AwardServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");

    @Override
    public Page<AwardDTO> getAwards(String email, Pageable pageable) {
        logger.info("getAwards() - Finding user products for award dto for user with email "+email);
        Page<Product> products = productRepository.findUserProducts(email,pageable);
        List<AwardDTO> awardDTOS = ProductMapper.PRODUCT_MAPPER.productListToAwardDTOList(products.getContent());
        String s = "https://slj.avada-media-dev1.od.ua/Coffee_App_A_Rudiuk/uploads/";
        for(AwardDTO award : awardDTOS){
            award.setImage(s+award.getImage());
        }
        Page<AwardDTO> awardDTOPage = new PageImpl<>(awardDTOS,pageable, products.getTotalElements());
        logger.info("getAwards() - User products for award dto were found");
        return awardDTOPage;
    }
}
