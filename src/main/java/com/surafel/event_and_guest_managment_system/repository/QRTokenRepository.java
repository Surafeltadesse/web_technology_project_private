package com.surafel.event_and_guest_managment_system.repository;

import com.surafel.event_and_guest_managment_system.entity.QRToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QRTokenRepository extends JpaRepository<QRToken, Long> {
    Optional<QRToken> findByToken(String token);
    Optional<QRToken> findByInvitationId(Long invitationId);
    boolean existsByInvitationId(Long invitationId);
}
