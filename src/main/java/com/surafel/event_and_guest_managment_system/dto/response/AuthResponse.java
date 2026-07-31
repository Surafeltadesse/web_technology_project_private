package com.surafel.event_and_guest_managment_system.dto.response;

import com.surafel.event_and_guest_managment_system.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String type;
    private Long userId;
    private String name;
    private String email;
    private Role role;
}
