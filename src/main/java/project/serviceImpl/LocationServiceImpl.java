package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Location;
import project.mapper.LocationMapper;
import project.model.locationModel.LocationAddressDTO;
import project.model.locationModel.LocationCoordinatesDTO;
import project.model.locationModel.LocationResponse;
import project.repository.LocationRepository;
import project.service.LocationService;
import static project.specification.LocationSpecification.*;
import java.util.List;
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");

    @Override
    public List<LocationCoordinatesDTO> getLocationCoordinates() {
        logger.info("getLocationCoordinates() - Finding locations for location coordinates");
        List<Location> locations = locationRepository.findAll(byDeleted());
        List<LocationCoordinatesDTO> locationCoordinatesDTOS = LocationMapper.LOCATION_MAPPER.locationListToLocationCoordinatesDTOList(locations);
        logger.info("getLocationCoordinates() - Locations for location coordinates were found");
        return locationCoordinatesDTOS;
    }

    @Override
    public LocationResponse getLocationResponseById(Long id) {
        logger.info("getLocationResponseById() - Finding location for location response by id "+id);
        Location location = locationRepository.findById(id).get();
        LocationResponse locationResponse = LocationMapper.LOCATION_MAPPER.locationToLocationResponse(location);
        logger.info("getLocationResponseById() - Location for location response was found");
        return locationResponse;
    }

    @Override
    public Page<LocationAddressDTO> getLocationAddresses(Pageable pageable) {
        logger.info("getLocationAddresses() - Finding locations for location address dto for page "+pageable.getPageNumber());
        Page<Location> locations = locationRepository.findAll(byDeleted(),pageable);
        List<LocationAddressDTO> locationAddressDTOS = LocationMapper.LOCATION_MAPPER.locationListToLocationAddressDTOList(locations.getContent());
        Page<LocationAddressDTO> locationAddressDTOPage = new PageImpl<>(locationAddressDTOS,pageable,locations.getTotalElements());
        logger.info("getLocationAddresses() - Locations for location address dto were found");
        return locationAddressDTOPage;
    }

    @Override
    public Location getLocationById(Long id) {
        logger.info("getLocationById() - Finding location by id "+id);
        Location location = locationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getLocationById() - Location was found");
        return location;
    }
}
