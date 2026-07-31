package com.surafel.event_and_guest_managment_system.service;

import com.surafel.event_and_guest_managment_system.entity.*;
import com.surafel.event_and_guest_managment_system.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void sendInvitation(Invitation invitation) {
        String body = String.format(
                "Dear %s,\n\nYou are invited to '%s' on %s.\nPlease RSVP via the EventGate portal.",
                invitation.getGuest().getFirstName(),
                invitation.getEvent().getTitle(),
                invitation.getEvent().getStartDate());

        Notification notification = Notification.builder()
                .recipientEmail(invitation.getGuest().getEmail())
                .type(NotificationType.INVITATION_SENT)
                .subject("You're Invited: " + invitation.getEvent().getTitle())
                .body(body).delivered(true)
                .eventId(invitation.getEvent().getId())
                .guestId(invitation.getGuest().getId())
                .invitationId(invitation.getId())
                .build();
        notificationRepository.save(notification);
        log.info("[NOTIFICATION] Invitation sent to {} for event '{}'",
                invitation.getGuest().getEmail(), invitation.getEvent().getTitle());
    }

    public void sendRsvpConfirmation(Invitation invitation) {
        NotificationType type = invitation.getStatus() == InvitationStatus.CONFIRMED
                ? NotificationType.RSVP_CONFIRMED : NotificationType.RSVP_DECLINED;
        String body = invitation.getStatus() == InvitationStatus.CONFIRMED
                ? String.format("Your attendance at '%s' has been confirmed. Your QR code will be sent shortly.",
                invitation.getEvent().getTitle())
                : String.format("We have received your RSVP decline for '%s'. We hope to see you next time!",
                invitation.getEvent().getTitle());

        Notification notification = Notification.builder()
                .recipientEmail(invitation.getGuest().getEmail())
                .type(type).subject("RSVP Update: " + invitation.getEvent().getTitle())
                .body(body).delivered(true)
                .eventId(invitation.getEvent().getId())
                .guestId(invitation.getGuest().getId())
                .invitationId(invitation.getId())
                .build();
        notificationRepository.save(notification);
        log.info("[NOTIFICATION] RSVP {} confirmation sent to {}",
                invitation.getStatus(), invitation.getGuest().getEmail());
    }

    public void sendCheckInSuccess(CheckInLog log2, Invitation invitation) {
        Notification notification = Notification.builder()
                .recipientEmail(invitation.getGuest().getEmail())
                .type(NotificationType.CHECKIN_SUCCESS)
                .subject("Check-in Confirmed: " + invitation.getEvent().getTitle())
                .body(String.format("You have successfully checked in to '%s' at %s.",
                        invitation.getEvent().getTitle(), log2.getCheckedInAt()))
                .delivered(true)
                .eventId(invitation.getEvent().getId())
                .guestId(invitation.getGuest().getId())
                .invitationId(invitation.getId())
                .build();
        notificationRepository.save(notification);
        log.info("[NOTIFICATION] Check-in success sent to {}", invitation.getGuest().getEmail());
    }

    public List<Notification> getByEventId(Long eventId) {
        return notificationRepository.findByEventId(eventId);
    }

}
