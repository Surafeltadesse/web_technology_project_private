package com.surafel.event_and_guest_managment_system.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.surafel.event_and_guest_managment_system.dto.request.ScanRequest;
import com.surafel.event_and_guest_managment_system.dto.response.CheckInResponse;
import com.surafel.event_and_guest_managment_system.dto.response.QRTokenResponse;
import com.surafel.event_and_guest_managment_system.entity.*;
import com.surafel.event_and_guest_managment_system.exception.*;
import com.surafel.event_and_guest_managment_system.repository.CheckInLogRepository;
import com.surafel.event_and_guest_managment_system.repository.InvitationRepository;
import com.surafel.event_and_guest_managment_system.repository.QRTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QRService {
    private final QRTokenRepository qrTokenRepository;
    private final InvitationRepository invitationRepository;
    private final CheckInLogRepository checkInLogRepository;
    private final NotificationService notificationService;

    @Value("${qr.output-dir}")
    private String outputDir;

    @Value("${qr.base-url}")
    private String baseUrl;

    @Transactional
    public QRTokenResponse generateQR(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found: " + invitationId));

        if (invitation.getStatus() != InvitationStatus.CONFIRMED) {
            throw new InvalidOperationException("QR code can only be generated for CONFIRMED invitations");
        }
        if (qrTokenRepository.existsByInvitationId(invitationId)) {
            return toResponse(qrTokenRepository.findByInvitationId(invitationId).orElseThrow());
        }

        String token = UUID.randomUUID().toString();
        String imagePath = generateQRImage(token, invitationId);

        QRToken qrToken = QRToken.builder()
                .token(token).qrImagePath(imagePath)
                .issuedAt(LocalDateTime.now())
                .expiresAt(invitation.getEvent().getEndDate())
                .isUsed(false).invitation(invitation)
                .build();

        return toResponse(qrTokenRepository.save(qrToken));
    }

    @Transactional
    public CheckInResponse scan(ScanRequest request, Long staffId) {
        QRToken qrToken = qrTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new InvalidQRTokenException("Invalid QR token"));

        if (qrToken.getIsUsed()) {
            throw new AlreadyCheckedInException("Guest has already been checked in");
        }
        if (qrToken.getExpiresAt() != null && LocalDateTime.now().isAfter(qrToken.getExpiresAt())) {
            throw new InvalidQRTokenException("QR token has expired");
        }

        Invitation invitation = qrToken.getInvitation();
        Event event = invitation.getEvent();

        if (event.getStatus() == EventStatus.CANCELLED || event.getStatus() == EventStatus.COMPLETED) {
            throw new InvalidOperationException("Event is no longer active");
        }

        // capacity check
        long currentCount = checkInLogRepository.countByInvitation_EventId(event.getId());
        if (currentCount >= event.getVenue().getCapacity()) {
            throw new EventFullException("Venue capacity reached for this event");
        }

        qrToken.setIsUsed(true);
        qrTokenRepository.save(qrToken);

        CheckInLog checkInLog = CheckInLog.builder()
                .invitation(invitation)
                .gateName(request.getGateName())
                .scannedByStaffId(staffId)
                .build();
        CheckInLog saved = checkInLogRepository.save(checkInLog);
        notificationService.sendCheckInSuccess(saved, invitation);

        Guest guest = invitation.getGuest();
        return CheckInResponse.builder()
                .id(saved.getId()).checkedInAt(saved.getCheckedInAt())
                .gateName(saved.getGateName()).scannedByStaffId(staffId)
                .invitationId(invitation.getId())
                .guestName(guest.getFirstName() + " " + guest.getLastName())
                .eventTitle(event.getTitle())
                .success(true).message("Check-in successful! Welcome, " + guest.getFirstName() + "!")
                .build();
    }

    public QRTokenResponse getByInvitation(Long invitationId) {
        return toResponse(qrTokenRepository.findByInvitationId(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("QR token not found for invitation: " + invitationId)));
    }

    private String generateQRImage(String token, Long invitationId) {
        try {
            Path dir = Paths.get(outputDir);
            Files.createDirectories(dir);
            String filename = "qr_" + invitationId + "_" + token.substring(0, 8) + ".png";
            Path filePath = dir.resolve(filename);
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(token, BarcodeFormat.QR_CODE, 300, 300);
            MatrixToImageWriter.writeToPath(matrix, "PNG", filePath);
            return filePath.toString();
        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code: {}", e.getMessage());
            throw new InvalidOperationException("Failed to generate QR code: " + e.getMessage());
        }
    }

    private QRTokenResponse toResponse(QRToken q) {
        return QRTokenResponse.builder()
                .id(q.getId()).token(q.getToken()).qrImagePath(q.getQrImagePath())
                .issuedAt(q.getIssuedAt()).expiresAt(q.getExpiresAt()).isUsed(q.getIsUsed())
                .invitationId(q.getInvitation().getId())
                .build();
    }
}
