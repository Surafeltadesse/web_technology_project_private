package com.surafel.event_and_guest_managment_system.controller;

import com.surafel.event_and_guest_managment_system.dto.request.InvitationRequest;
import com.surafel.event_and_guest_managment_system.dto.request.RsvpRequest;
import com.surafel.event_and_guest_managment_system.dto.response.ApiResponse;
import com.surafel.event_and_guest_managment_system.dto.response.InvitationResponse;
import com.surafel.event_and_guest_managment_system.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<InvitationResponse>> create(
            @Valid @RequestBody InvitationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Invitation sent", invitationService.create(request)));
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<List<InvitationResponse>>> getByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.success(invitationService.getByEvent(eventId)));
    }

    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER','GUEST')")
    public ResponseEntity<ApiResponse<List<InvitationResponse>>> getByGuest(@PathVariable Long guestId) {
        return ResponseEntity.ok(ApiResponse.success(invitationService.getByGuest(guestId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvitationResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(invitationService.getById(id)));
    }

    @PutMapping("/{id}/rsvp")
    public ResponseEntity<ApiResponse<InvitationResponse>> rsvp(
            @PathVariable Long id, @Valid @RequestBody RsvpRequest request) {
        return ResponseEntity.ok(ApiResponse.success("RSVP submitted", invitationService.rsvp(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        invitationService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Invitation revoked").build());
    }
}
