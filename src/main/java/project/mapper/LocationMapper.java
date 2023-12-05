package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.entity.Location;
import project.model.locationModel.LocationAddressDTO;
import project.model.locationModel.LocationCoordinatesDTO;
import project.model.locationModel.LocationResponse;

import java.util.List;
@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationMapper LOCATION_MAPPER = Mappers.getMapper(LocationMapper.class);
    List<LocationCoordinatesDTO> locationListToLocationCoordinatesDTOList(List<Location> locations);
    LocationResponse locationToLocationResponse(Location location);
    List<LocationAddressDTO> locationListToLocationAddressDTOList(List<Location> locations);
}
