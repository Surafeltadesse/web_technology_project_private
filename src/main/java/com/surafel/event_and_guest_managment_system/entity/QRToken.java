package com.surafel.event_and_guest_managment_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "qr_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QRToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,  unique = true)
    private String token;

    private String qrImagePath;

    @Column(nullable = false)
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean isUsed;

    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitation_id", nullable = false, unique = true)
    private Invitation invitation;

    // LifeCycle
    @PrePersist
    protected void onCreate() {
        issuedAt = LocalDateTime.now();
        if (isUsed == null) isUsed = false;
    }
}
