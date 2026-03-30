package com.healthcare.system.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Patient extends User {

    private int age;
    private String gender;

    // ================= RELATIONSHIPS =================

    @OneToMany(mappedBy = "patient")
    @JsonManagedReference
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient")
    private List<VisitRecord> visitRecords;

    // ================= GETTERS & SETTERS =================

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<VisitRecord> getVisitRecords() {
        return visitRecords;
    }

    public void setVisitRecords(List<VisitRecord> visitRecords) {
        this.visitRecords = visitRecords;
    }
}