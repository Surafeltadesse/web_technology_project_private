package com.surafel.event_and_guest_managment_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "venues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotBlank
    private String city;

    private String country;

    @Min(1)
    @Column(nullable = false)
    private Integer capacity;

    private String contactPhone;
    private String contactEmail;

    // Relationships

    @OneToMany(mappedBy = "venue", fetch = FetchType.LAZY)
    private List<Event> events;
}
