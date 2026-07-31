package com.surafel.event_and_guest_managment_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuestResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long userId;
}
