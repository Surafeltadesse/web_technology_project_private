package com.surafel.event_and_guest_managment_system.repository;

import com.surafel.event_and_guest_managment_system.entity.Invitation;
import com.surafel.event_and_guest_managment_system.entity.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByEventId(Long eventId);
    List<Invitation> findByGuestId(Long guestId);
    List<Invitation> findByEventIdAndStatus(Long eventId, InvitationStatus status);
    Optional<Invitation>findByEventIdAndGuestId(Long eventId, Long guestId);

    Boolean existsByEventIdAndGuestId(Long eventId, Long guestId);

    @Query("SELECT COUNT(i) FROM Invitation i WHERE i.event.id = :eventId AND i.status = 'CONFIRMED'")
    long countConfirmedByEventId(@Param("eventId") Long eventId);
}
