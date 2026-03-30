package com.healthcare.system.model;

import jakarta.persistence.*;
//import java.time.LocalDateTime;
@Entity
public class Admin extends User {

    private String adminLevel;

    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }
}