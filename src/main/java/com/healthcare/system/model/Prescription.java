package com.healthcare.system.model;

import jakarta.persistence.*;
import java.util.List;
//import java.time.LocalDateTime;
@Entity
public class Prescription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String notes;

    @ManyToOne private VisitRecord visitRecord;
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL)
    private List<MedicineItem> medicines;

    public Long getId() { return id; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public VisitRecord getVisitRecord() { return visitRecord; }
    public void setVisitRecord(VisitRecord v) { this.visitRecord = v; }
    public List<MedicineItem> getMedicines() { return medicines; }
    public void setMedicines(List<MedicineItem> m) { this.medicines = m; }
}