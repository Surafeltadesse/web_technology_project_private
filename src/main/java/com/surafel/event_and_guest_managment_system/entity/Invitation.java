package com.surafel.event_and_guest_managment_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "invitations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "guest_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @Column(nullable = false)
    private Boolean plusOneAllowed;

    private String seatAssignment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime invitedAt;

    private LocalDateTime respondedAt;

    // Relationships

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id",  nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id",  nullable = false)
    private Guest guest;

    @OneToOne(mappedBy = "invitation", cascade = CascadeType.ALL)
    private QRToken qrToken;

    @OneToOne(mappedBy = "invitation", cascade = CascadeType.ALL)
    private CheckInLog checkInLog;

    // Lifecycle
    @PrePersist
    protected void onCreate() {
        invitedAt = LocalDateTime.now();
        if (status == null) status = InvitationStatus.PENDING;
        if (plusOneAllowed == null) plusOneAllowed = false;
    }

}
