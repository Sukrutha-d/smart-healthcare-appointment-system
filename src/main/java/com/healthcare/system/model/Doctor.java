package com.healthcare.system.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Doctor extends User {

    private String specialization;

    private String availableFrom;
    private String availableTo;

    // ================= RELATIONSHIPS =================

    // ✅ ONLY KEEP SLOT RELATION
    @OneToMany(mappedBy = "doctor")
    @JsonManagedReference
    private List<AvailabilitySlot> slots;

    // ================= GETTERS & SETTERS =================

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }

    public String getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(String availableTo) {
        this.availableTo = availableTo;
    }

    public List<AvailabilitySlot> getSlots() {
        return slots;
    }

    public void setSlots(List<AvailabilitySlot> slots) {
        this.slots = slots;
    }
}