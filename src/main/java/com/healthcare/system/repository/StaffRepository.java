package com.healthcare.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.system.model.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findByName(String name);
    Staff findByEmail(String email);
}
