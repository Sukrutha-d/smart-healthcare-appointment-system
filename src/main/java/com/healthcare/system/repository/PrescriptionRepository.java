package com.healthcare.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.system.model.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
