package com.surafel.event_and_guest_managment_system.service;

import com.surafel.event_and_guest_managment_system.dto.request.EventRequest;
import com.surafel.event_and_guest_managment_system.dto.response.EventResponse;
import com.surafel.event_and_guest_managment_system.dto.response.UserResponse;
import com.surafel.event_and_guest_managment_system.entity.*;
import com.surafel.event_and_guest_managment_system.exception.InvalidOperationException;
import com.surafel.event_and_guest_managment_system.exception.ResourceNotFoundException;
import com.surafel.event_and_guest_managment_system.exception.UnauthorizedAccessException;
import com.surafel.event_and_guest_managment_system.repository.EventRepository;
import com.surafel.event_and_guest_managment_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final VenueService venueService;

    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public EventResponse create(EventRequest request, String organizerEmail) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new InvalidOperationException("End date must be after start date");
        }
        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Venue venue = venueService.findById(request.getVenueId());
        Event event = Event.builder()
                .title(request.getTitle()).description(request.getDescription())
                .eventType(request.getEventType()).status(EventStatus.DRAFT)
                .startDate(request.getStartDate()).endDate(request.getEndDate())
                .organizer(organizer).venue(venue)
                .build();
        return toResponse(eventRepository.save(event));
    }

    public List<EventResponse> getAll() {
        return eventRepository.findAll().stream().map(this::toResponse).toList();
    }

    public EventResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public List<EventResponse> getByOrganizer(Long organizerId) {
        return eventRepository.findByOrganizerId(organizerId).stream().map(this::toResponse).toList();
    }

    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public EventResponse update(Long id, EventRequest request, String currentUserEmail) {
        Event event = findById(id);
        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow();
        if (!event.getOrganizer().getId().equals(currentUser.getId())
                && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("You can only update your own events");
        }
        event.setTitle(request.getTitle()); event.setDescription(request.getDescription());
        event.setEventType(request.getEventType());
        event.setStartDate(request.getStartDate()); event.setEndDate(request.getEndDate());
        event.setVenue(venueService.findById(request.getVenueId()));
        return toResponse(eventRepository.save(event));
    }

    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public EventResponse updateStatus(Long id, EventStatus status, String currentUserEmail) {
        Event event = findById(id);
        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow();
        if (!event.getOrganizer().getId().equals(currentUser.getId())
                && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("You can only update your own events");
        }
        event.setStatus(status);
        return toResponse(eventRepository.save(event));
    }

    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public void delete(Long id, String currentUserEmail) {
        Event event = findById(id);
        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow();
        if (!event.getOrganizer().getId().equals(currentUser.getId())
                && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("You can only delete your own events");
        }
        eventRepository.delete(event);
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));
    }

    public EventResponse toResponse(Event e) {
        User org = e.getOrganizer();
        return EventResponse.builder()
                .id(e.getId()).title(e.getTitle()).description(e.getDescription())
                .eventType(e.getEventType()).status(e.getStatus())
                .startDate(e.getStartDate()).endDate(e.getEndDate())
                .createdAt(e.getCreatedAt())
                .confirmedGuestCount(eventRepository.countConfirmedGuests(e.getId()))
                .organizer(UserResponse.builder().id(org.getId()).name(org.getName())
                        .email(org.getEmail()).role(org.getRole()).build())
                .venue(venueService.toResponse(e.getVenue()))
                .build();
    }
}
