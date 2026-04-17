package com.healthcare.system.model;

import jakarta.persistence.Entity;

@Entity
public class Patient extends User {

    // Removed duplicate name field to fix shadowing (User class already has name)

}