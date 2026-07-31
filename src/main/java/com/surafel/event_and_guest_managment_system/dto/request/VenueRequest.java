package com.surafel.event_and_guest_managment_system.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VenueRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    private String country;

    @Min(value = 1, message = "Capacity must be at least 1")
    @NotNull(message = "Capacity is required")
    private Integer capacity;

    private String contactPhone;
    private String contactEmail;
}
