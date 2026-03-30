package com.healthcare.system.model;
import jakarta.persistence.*;
//import java.time.LocalDateTime;
@Entity
public class Staff extends User {

    private String department;

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}