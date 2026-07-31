package com.surafel.event_and_guest_managment_system.service;

import com.surafel.event_and_guest_managment_system.dto.request.AgendaSessionRequest;
import com.surafel.event_and_guest_managment_system.dto.response.AgendaSessionResponse;
import com.surafel.event_and_guest_managment_system.entity.AgendaSession;
import com.surafel.event_and_guest_managment_system.entity.Event;
import com.surafel.event_and_guest_managment_system.entity.EventType;
import com.surafel.event_and_guest_managment_system.exception.InvalidOperationException;
import com.surafel.event_and_guest_managment_system.exception.ResourceNotFoundException;
import com.surafel.event_and_guest_managment_system.repository.AgendaSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaSessionService {
    private final AgendaSessionRepository agendaSessionRepository;
    private final EventService eventService;

    public AgendaSessionResponse create(AgendaSessionRequest request) {
        Event event = eventService.findById(request.getEventId());
        if (event.getEventType() != EventType.CONFERENCE) {
            throw new InvalidOperationException("Agenda sessions are only allowed for CONFERENCE events");
        }
        AgendaSession session = AgendaSession.builder()
                .event(event).sessionTitle(request.getSessionTitle())
                .speaker(request.getSpeaker()).location(request.getLocation())
                .description(request.getDescription())
                .startTime(request.getStartTime()).endTime(request.getEndTime())
                .build();
        return toResponse(agendaSessionRepository.save(session));
    }

    public List<AgendaSessionResponse> getByEvent(Long eventId) {
        return agendaSessionRepository.findByEventIdOrderByStartTimeAsc(eventId)
                .stream().map(this::toResponse).toList();
    }

    public AgendaSessionResponse update(Long id, AgendaSessionRequest request) {
        AgendaSession session = agendaSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + id));
        session.setSessionTitle(request.getSessionTitle());
        session.setSpeaker(request.getSpeaker()); session.setLocation(request.getLocation());
        session.setDescription(request.getDescription());
        session.setStartTime(request.getStartTime()); session.setEndTime(request.getEndTime());
        return toResponse(agendaSessionRepository.save(session));
    }

    public void delete(Long id) {
        agendaSessionRepository.deleteById(id);
    }

    private AgendaSessionResponse toResponse(AgendaSession s) {
        return AgendaSessionResponse.builder()
                .id(s.getId()).sessionTitle(s.getSessionTitle())
                .speaker(s.getSpeaker()).location(s.getLocation())
                .description(s.getDescription())
                .startTime(s.getStartTime()).endTime(s.getEndTime())
                .eventId(s.getEvent().getId())
                .build();
    }
}