package com.surafel.event_and_guest_managment_system.controller;

import com.surafel.event_and_guest_managment_system.dto.request.AgendaSessionRequest;
import com.surafel.event_and_guest_managment_system.dto.response.AgendaSessionResponse;
import com.surafel.event_and_guest_managment_system.dto.response.ApiResponse;
import com.surafel.event_and_guest_managment_system.service.AgendaSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendas")
@RequiredArgsConstructor
public class AgendaController {
    private final AgendaSessionService agendaSessionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<AgendaSessionResponse>> create(
            @Valid @RequestBody AgendaSessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Session created", agendaSessionService.create(request)));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponse<List<AgendaSessionResponse>>> getByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.success(agendaSessionService.getByEvent(eventId)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<AgendaSessionResponse>> update(
            @PathVariable Long id, @Valid @RequestBody AgendaSessionRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Session updated", agendaSessionService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        agendaSessionService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Session deleted").build());
    }
}
