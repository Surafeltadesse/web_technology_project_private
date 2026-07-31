package com.surafel.event_and_guest_managment_system.dto.response;

import com.surafel.event_and_guest_managment_system.entity.InvitationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InvitationResponse {
    private Long id;
    private InvitationStatus status;
    private Boolean plusOneAllowed;
    private String seatAssignment;
    private LocalDateTime invitedAt;
    private LocalDateTime respondedAt;
    private Long eventId;
    private String eventTitle;
    private GuestResponse guest;
    private String qrImagePath;
}
