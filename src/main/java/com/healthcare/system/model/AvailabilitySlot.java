package com.healthcare.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;  // ✅ IMPORTANT
import com.healthcare.system.enums.SlotStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    private String patientName;

    @ManyToOne
    @JoinColumn(name = "doctor_id")

    @JsonIgnore   // 🔥 THIS LINE FIXES YOUR ISSUE
    private Doctor doctor;

    @Transient
    private com.healthcare.system.enums.AppointmentStatus appointmentStatus;

    public com.healthcare.system.enums.AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(com.healthcare.system.enums.AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}