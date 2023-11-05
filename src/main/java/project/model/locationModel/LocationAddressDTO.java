package project.model.locationModel;

import io.swagger.v3.oas.annotations.media.Schema;

public class LocationAddressDTO {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "вул.Васильківська 5", required = true)
    private String address;
    @Schema(example = "Київ", required = true)
    private String city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
