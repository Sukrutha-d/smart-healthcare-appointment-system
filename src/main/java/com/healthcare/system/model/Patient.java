package com.healthcare.system.model;

import jakarta.persistence.Entity;

@Entity
public class Patient extends User {

    private String name;

    // ================= GETTERS & SETTERS =================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}