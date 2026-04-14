package com.healthcare.system.repository;

import com.healthcare.system.model.AvailabilitySlot;
import com.healthcare.system.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByDoctorOrderByStartTimeAsc(Doctor doctor);

    boolean existsByDoctorAndStartTime(Doctor doctor, LocalDateTime startTime);
}