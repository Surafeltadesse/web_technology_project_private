package com.surafel.event_and_guest_managment_system.repository;

import com.surafel.event_and_guest_managment_system.entity.Event;
import com.surafel.event_and_guest_managment_system.entity.EventStatus;
import com.surafel.event_and_guest_managment_system.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganizerId(Long organizerId);
    List<Event> findByStatus(EventStatus status);
    List<Event> findByEventType(EventType eventType);
    List<Event> findByOrganizerIdAndStatus(Long organizerId, EventStatus status);

    @Query("SELECT COUNT(i) FROM Invitation i WHERE i.event.id = :eventId AND i.status = 'CONFIRMED'")
    Long countConfirmedGuests(@Param("eventId") Long eventId);
}
