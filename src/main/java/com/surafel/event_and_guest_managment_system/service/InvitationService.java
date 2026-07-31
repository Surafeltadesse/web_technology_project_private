package com.surafel.event_and_guest_managment_system.service;

import com.surafel.event_and_guest_managment_system.dto.request.InvitationRequest;
import com.surafel.event_and_guest_managment_system.dto.request.RsvpRequest;
import com.surafel.event_and_guest_managment_system.dto.response.InvitationResponse;
import com.surafel.event_and_guest_managment_system.entity.Event;
import com.surafel.event_and_guest_managment_system.entity.Guest;
import com.surafel.event_and_guest_managment_system.entity.Invitation;
import com.surafel.event_and_guest_managment_system.entity.InvitationStatus;
import com.surafel.event_and_guest_managment_system.exception.DuplicateResourceException;
import com.surafel.event_and_guest_managment_system.exception.EventFullException;
import com.surafel.event_and_guest_managment_system.exception.InvalidOperationException;
import com.surafel.event_and_guest_managment_system.exception.ResourceNotFoundException;
import com.surafel.event_and_guest_managment_system.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final EventService eventService;
    private final GuestService guestService;
    private final NotificationService notificationService;

    @Transactional
    public InvitationResponse create(InvitationRequest request) {
        if (invitationRepository.existsByEventIdAndGuestId(request.getEventId(), request.getGuestId())) {
            throw new DuplicateResourceException("Guest already invited to this event");
        }
        Event event = eventService.findById(request.getEventId());
        Guest guest = guestService.findById(request.getGuestId());

        // check capacity
        long confirmed = invitationRepository.countConfirmedByEventId(event.getId());
        if (confirmed >= event.getVenue().getCapacity()) {
            throw new EventFullException("Event venue is at full capacity");
        }

        Invitation invitation = Invitation.builder()
                .event(event).guest(guest)
                .plusOneAllowed(request.getPlusOneAllowed())
                .seatAssignment(request.getSeatAssignment())
                .status(InvitationStatus.PENDING)
                .build();
        Invitation saved = invitationRepository.save(invitation);

        // fire-and-forget notification
        notificationService.sendInvitation(saved);

        return toResponse(saved);
    }

    public List<InvitationResponse> getByEvent(Long eventId) {
        return invitationRepository.findByEventId(eventId).stream().map(this::toResponse).toList();
    }

    public List<InvitationResponse> getByGuest(Long guestId) {
        return invitationRepository.findByGuestId(guestId).stream().map(this::toResponse).toList();
    }

    public InvitationResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public InvitationResponse rsvp(Long id, RsvpRequest request) {
        Invitation invitation = findById(id);
        if (invitation.getStatus() == InvitationStatus.CONFIRMED
                || invitation.getStatus() == InvitationStatus.DECLINED) {
            throw new InvalidOperationException("RSVP has already been submitted");
        }
        if (request.getStatus() != InvitationStatus.CONFIRMED
                && request.getStatus() != InvitationStatus.DECLINED) {
            throw new InvalidOperationException("RSVP status must be CONFIRMED or DECLINED");
        }
        invitation.setStatus(request.getStatus());
        invitation.setRespondedAt(LocalDateTime.now());
        Invitation saved = invitationRepository.save(invitation);
        notificationService.sendRsvpConfirmation(saved);
        return toResponse(saved);
    }

    public void delete(Long id) {
        invitationRepository.delete(findById(id));
    }

    public Invitation findById(Long id) {
        return invitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found: " + id));
    }

    public InvitationResponse toResponse(Invitation i) {
        return InvitationResponse.builder()
                .id(i.getId()).status(i.getStatus())
                .plusOneAllowed(i.getPlusOneAllowed())
                .seatAssignment(i.getSeatAssignment())
                .invitedAt(i.getInvitedAt()).respondedAt(i.getRespondedAt())
                .eventId(i.getEvent().getId()).eventTitle(i.getEvent().getTitle())
                .guest(guestService.toResponse(i.getGuest()))
                .qrImagePath(i.getQrToken() != null ? i.getQrToken().getQrImagePath() : null)
                .build();
    }
}
