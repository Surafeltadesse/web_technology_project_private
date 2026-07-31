package com.surafel.event_and_guest_managment_system.controller;

import com.surafel.event_and_guest_managment_system.dto.request.VenueRequest;
import com.surafel.event_and_guest_managment_system.dto.response.ApiResponse;
import com.surafel.event_and_guest_managment_system.dto.response.VenueResponse;
import com.surafel.event_and_guest_managment_system.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<VenueResponse>> create(@Valid @RequestBody VenueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Venue created", venueService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VenueResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(venueService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VenueResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(venueService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<ApiResponse<VenueResponse>> update(
            @PathVariable Long id, @Valid @RequestBody VenueRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Venue updated", venueService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Venue deleted").build());
    }
}
