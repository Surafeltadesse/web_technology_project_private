package com.surafel.event_and_guest_managment_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendaRequest {
    @NotBlank(message = "Session title is required")
    private String sessionTitle;

    private String speaker;
    private String description;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private String location;

    @NotNull(message = "Event ID is required")
    private Long eventId;
}
