package com.healthcare.system.repository;

import com.healthcare.system.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByName(String name);

    Patient findByEmail(String email);
}