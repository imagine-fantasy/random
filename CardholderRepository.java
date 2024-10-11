package com.yourcompany.ccards.repository;

import com.yourcompany.ccards.model.Cardholder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CardholderRepository extends JpaRepository<Cardholder, UUID> {
}
