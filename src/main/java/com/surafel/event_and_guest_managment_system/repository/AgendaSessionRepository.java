package com.surafel.event_and_guest_managment_system.repository;

import com.surafel.event_and_guest_managment_system.entity.AgendaSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaSessionRepository extends JpaRepository<AgendaSession, Long> {
    List<AgendaSession> findByEventIdOrderByStartTimeAsc(Long eventId);
    void  deleteByEventId(Long eventId);
}
