package com.surafel.event_and_guest_managment_system.repository;

import com.surafel.event_and_guest_managment_system.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByEventIdOrderByStartTimeAsc(Long eventId);
    void deleteByEventId(Long eventId);
}
