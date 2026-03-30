package com.healthcare.system.repository;

import com.healthcare.system.model.Appointment;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByDoctorNameAndAppointmentTime(String doctorName, LocalDateTime appointmentTime);

}