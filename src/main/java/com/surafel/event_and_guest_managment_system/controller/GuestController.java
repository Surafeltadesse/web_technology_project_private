package com.surafel.event_and_guest_managment_system.controller;

import com.surafel.event_and_guest_managment_system.dto.request.GuestRequest;
import com.surafel.event_and_guest_managment_system.dto.response.ApiResponse;
import com.surafel.event_and_guest_managment_system.dto.response.GuestResponse;
import com.surafel.event_and_guest_managment_system.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {
    private final GuestService guestService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<GuestResponse>> create(@Valid @RequestBody GuestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Guest created", guestService.create(request)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<List<GuestResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(guestService.getAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<GuestResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(guestService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<GuestResponse>> update(
            @PathVariable Long id, @Valid @RequestBody GuestRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Guest updated", guestService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        guestService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Guest deleted").build());
    }
}
