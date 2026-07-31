package com.surafel.event_and_guest_managment_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VenueResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String country;
    private Integer capacity;
    private String contactPhone;
    private String contactEmail;
}
