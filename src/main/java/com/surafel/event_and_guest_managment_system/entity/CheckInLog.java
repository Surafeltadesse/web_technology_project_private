package com.surafel.event_and_guest_managment_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkin_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime checkedInAt;

    private String gateName;

    private Long scannedByStaffId;

    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitation_id", nullable = false, unique = true)
    private Invitation invitation;

    // LifeCycle
    @PrePersist
    protected void onCreate() {
        checkedInAt = LocalDateTime.now();
    }
}
