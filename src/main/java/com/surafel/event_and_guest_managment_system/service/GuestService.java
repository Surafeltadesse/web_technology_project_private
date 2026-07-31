package com.surafel.event_and_guest_managment_system.service;

import com.surafel.event_and_guest_managment_system.dto.request.GuestRequest;
import com.surafel.event_and_guest_managment_system.dto.response.GuestResponse;
import com.surafel.event_and_guest_managment_system.entity.Guest;
import com.surafel.event_and_guest_managment_system.exception.DuplicateResourceException;
import com.surafel.event_and_guest_managment_system.exception.ResourceNotFoundException;
import com.surafel.event_and_guest_managment_system.repository.GuestRepository;
import com.surafel.event_and_guest_managment_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;

    public GuestResponse create(GuestRequest request) {
        if (guestRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Guest already exists with email: " + request.getEmail());
        }
        Guest guest = Guest.builder()
                .firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).phone(request.getPhone())
                .build();
        if (request.getUserId() != null) {
            guest.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId())));
        }
        return toResponse(guestRepository.save(guest));
    }

    public List<GuestResponse> getAll() {
        return guestRepository.findAll().stream().map(this::toResponse).toList();
    }

    public GuestResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public GuestResponse update(Long id, GuestRequest request) {
        Guest guest = findById(id);
        guest.setFirstName(request.getFirstName()); guest.setLastName(request.getLastName());
        guest.setPhone(request.getPhone());
        return toResponse(guestRepository.save(guest));
    }

    public void delete(Long id) {
        guestRepository.delete(findById(id));
    }

    public Guest findById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found: " + id));
    }

    public GuestResponse toResponse(Guest g) {
        return GuestResponse.builder()
                .id(g.getId()).firstName(g.getFirstName()).lastName(g.getLastName())
                .email(g.getEmail()).phone(g.getPhone())
                .userId(g.getUser() != null ? g.getUser().getId() : null)
                .build();
    }
}
