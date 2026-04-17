package com.healthcare.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.system.model.VisitRecord;

@Repository
public interface VisitRecordRepository extends JpaRepository<VisitRecord, Long> {
}
