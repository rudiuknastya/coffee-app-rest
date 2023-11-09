package project.model.deliveryModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import project.entity.Payment;

import java.time.LocalDate;
import java.time.LocalTime;

public class DeliveryRequest {
    @Schema(example = "Софія", required = true)
    @NotEmpty(message = "Поле не може бути порожнім")
    private String name;
    @Schema(example = "+380991452341", required = true)
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(min=4, max=15, message = "Розмір поля має бути не менше 4 та не більше 15 символів")
    @Pattern(regexp = "^\\+?[1-9][0-9]{4,15}$", message = "Невірний формат номеру")
    private String phoneNumber;
    @Schema(example = "Київ", required = true)
    @NotEmpty(message = "Поле не може бути порожнім")
    private String city;
    @Schema(example = "12б", required = true)
    @NotEmpty(message = "Поле не може бути порожнім")
    private String building;
    @Schema(example = "Вулиця", required = true)
    @NotEmpty(message = "Поле не може бути порожнім")
    private String street;
    @Schema(example = "5", required = true)
    @NotEmpty(message = "Поле не може бути порожнім")
    private String entrance;
    @Schema(example = "20", required = true)
    @NotNull(message = "Поле не може бути порожнім")
    private Long apartment;
    @NotNull(message = "Поле не може бути порожнім")
    private Payment payment;
    private Long remainderFrom;
    @Schema(example = "2023-09-13", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;
    @Schema(example = "19:00", required = true)
    @DateTimeFormat(pattern = "hh:mm:ss")
    private LocalTime deliveryTime;
    private boolean callBack;

    public boolean getCallBack() {
        return callBack;
    }

    public void setCallBack(boolean callBack) {
        this.callBack = callBack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public Long getApartment() {
        return apartment;
    }

    public void setApartment(Long apartment) {
        this.apartment = apartment;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Long getRemainderFrom() {
        return remainderFrom;
    }

    public void setRemainderFrom(Long remainderFrom) {
        this.remainderFrom = remainderFrom;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
