// package com.healthcare.system.model;

// import com.healthcare.system.enums.SlotStatus;
// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// public class AvailabilitySlot {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private LocalDateTime startTime;
//     private LocalDateTime endTime;

//     @Enumerated(EnumType.STRING)
//     private SlotStatus status;

//     @ManyToOne
//     private Doctor doctor;

//     public Long getId() { return id; }

//     public LocalDateTime getStartTime() { return startTime; }
//     public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

//     public LocalDateTime getEndTime() { return endTime; }
//     public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

//     public SlotStatus getStatus() { return status; }
//     public void setStatus(SlotStatus status) { this.status = status; }

//     public Doctor getDoctor() { return doctor; }
//     public void setDoctor(Doctor doctor) { this.doctor = doctor; }
// }

package com.healthcare.system.model;

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

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // ================= GETTERS & SETTERS =================

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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}