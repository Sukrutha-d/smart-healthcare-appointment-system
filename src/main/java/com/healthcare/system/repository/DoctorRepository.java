package com.healthcare.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.system.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByName(String name);
}