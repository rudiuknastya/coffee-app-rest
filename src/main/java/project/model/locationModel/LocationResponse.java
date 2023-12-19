package project.model.locationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;

public class LocationResponse {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "Київ", required = true)
    private String city;
    @Schema(example = "вул.Васильківська 5", required = true)
    private String address;
    @Schema(example = "+380994657718", required = true)
    private String phoneNumber;
    @Schema(example = "'пн-пт: 8:00-21:00 " +
            "сб-нд: 9:00-20:00'", required = true)
    private String workingHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }
}
