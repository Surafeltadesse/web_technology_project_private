package com.surafel.event_and_guest_managment_system.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InvitationRequest {
    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Guest ID is required")
    private Long guestId;

    private Boolean plusOneAllowed = false;
    private String seatAssignment;
}
