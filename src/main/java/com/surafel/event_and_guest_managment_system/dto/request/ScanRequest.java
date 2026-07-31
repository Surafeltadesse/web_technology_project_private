package com.surafel.event_and_guest_managment_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ScanRequest {
    @NotBlank(message = "QR token is required")
    private String token;

    private String gateName;
}
