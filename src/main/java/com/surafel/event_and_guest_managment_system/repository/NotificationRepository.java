package com.surafel.event_and_guest_managment_system.repository;

import com.surafel.event_and_guest_managment_system.entity.Notification;
import com.surafel.event_and_guest_managment_system.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEventId(Long eventId);
    List<Notification> findByGuestId(Long guestId);
    List<Notification> findByRecipientEmail(String email);
    List<Notification> findByType(NotificationType type);
}
