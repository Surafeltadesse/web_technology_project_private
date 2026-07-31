package com.surafel.event_and_guest_managment_system.dto.response;

import com.surafel.event_and_guest_managment_system.entity.EventStatus;
import com.surafel.event_and_guest_managment_system.entity.EventType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private EventType eventType;
    private EventStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private UserResponse organizer;
    private VenueResponse venue;
    private long confirmedGuestCount;
}
