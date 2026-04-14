package com.healthcare.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.model.User;
import com.healthcare.system.model.Doctor;
import com.healthcare.system.model.Patient;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public User login(String username, String password) {

        System.out.println("LOGIN TRY: " + username);

        // 🔍 Check doctor
        Doctor doctor = doctorRepository.findByEmail(username);
        if (doctor != null) {
            System.out.println("Doctor found");
            if (doctor.getPassword().equals(password)) {
                return doctor;
            }
        }

        // 🔍 Check patient
        Patient patient = patientRepository.findByEmail(username);
        if (patient != null) {
            System.out.println("Patient found");
            if (patient.getPassword().equals(password)) {
                return patient;
            }
        }

        System.out.println("Login failed");
        return null;
    }
}