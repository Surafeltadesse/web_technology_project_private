package com.surafel.event_and_guest_managment_system.dto.request;

import com.surafel.event_and_guest_managment_system.entity.InvitationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RsvpRequest {
    @NotNull(message = "Status is required")
    private InvitationStatus status;
}
