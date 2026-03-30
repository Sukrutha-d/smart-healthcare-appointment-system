package com.healthcare.system.model;

import jakarta.persistence.*;
//import java.time.LocalDateTime;
@Entity
public class MedicineItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String medicineName;
    private String dosage;
    private int durationDays;

    @ManyToOne private Prescription prescription;

    public Long getId() { return id; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String m) { this.medicineName = m; }
    public String getDosage() { return dosage; }
    public void setDosage(String d) { this.dosage = d; }
    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int d) { this.durationDays = d; }
    public Prescription getPrescription() { return prescription; }
    public void setPrescription(Prescription p) { this.prescription = p; }
}