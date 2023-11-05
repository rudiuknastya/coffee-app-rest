package project.model.locationModel;

import io.swagger.v3.oas.annotations.media.Schema;

public class LocationCoordinatesDTO {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "50.4059482889101, 30.611355706101573", required = true)
    private String coordinates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
