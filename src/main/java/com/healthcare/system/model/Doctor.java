package com.healthcare.system.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Doctor extends User {

    private String specialization;

    // ================= RELATIONSHIPS =================

    @OneToMany(mappedBy = "doctor")
    @JsonManagedReference
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor")
    private List<AvailabilitySlot> slots;

    // ================= GETTERS & SETTERS =================

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<AvailabilitySlot> getSlots() {
        return slots;
    }

    public void setSlots(List<AvailabilitySlot> slots) {
        this.slots = slots;
    }
}