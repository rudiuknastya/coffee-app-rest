package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.locationModel.LocationAddressDTO;
import project.model.locationModel.LocationCoordinatesDTO;
import project.model.locationModel.LocationResponse;

import java.util.List;

public interface LocationService {
    List<LocationCoordinatesDTO> getLocationCoordinates();
    LocationResponse getLocationResponseById(Long id);
    Page<LocationAddressDTO> getLocationAddresses(Pageable pageable);
}
