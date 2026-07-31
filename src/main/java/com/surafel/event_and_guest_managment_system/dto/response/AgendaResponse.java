package com.surafel.event_and_guest_managment_system.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendaResponse {
    private Long id;
    private String sessionTitle;
    private String speaker;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private Long eventId;
    private LocalDateTime createdAt;
}
