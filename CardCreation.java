package com.yourcompany.ccards.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "card_creations", schema = "cc_cards")
public class CardCreation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cardholder_id", nullable = false)
    private Cardholder cardholder;

    @Column(name = "request_date", nullable = false)
    private OffsetDateTime requestDate;

    @Column(name = "processor_response", nullable = false, columnDefinition = "jsonb")
    private String processorResponse;

    @Column(nullable = false)
    private String status;

    // Getters and setters
}
