package com.surafel.event_and_guest_managment_system.repository;

import com.surafel.event_and_guest_managment_system.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Guest> findByUserId(Long userId);
}
