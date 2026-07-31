package com.surafel.event_and_guest_managment_system.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QRTokenResponse {
    private Long id;
    private String token;
    private String qrImagePath;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private Boolean isUsed;
    private Long invitationId;
}
