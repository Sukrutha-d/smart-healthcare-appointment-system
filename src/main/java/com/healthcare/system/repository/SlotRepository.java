package com.healthcare.system.repository;

import com.healthcare.system.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotRepository extends JpaRepository<AvailabilitySlot, Long> {
}