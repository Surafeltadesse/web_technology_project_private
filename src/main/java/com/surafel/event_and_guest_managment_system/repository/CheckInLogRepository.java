package com.surafel.event_and_guest_managment_system.repository;

import com.surafel.event_and_guest_managment_system.entity.CheckInLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInLogRepository extends JpaRepository<CheckInLog, Long> {
    Optional<CheckInLog> findByInvitationId(Long invitationId);
    Boolean existsByInvitationId(Long invitationId);

    List<CheckInLog> findByInvitation_EventId(Long EventId);
    Long countByInvitation_EventId(Long EventId);
}
