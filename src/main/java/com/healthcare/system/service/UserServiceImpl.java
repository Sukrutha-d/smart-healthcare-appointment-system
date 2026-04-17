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

    @Autowired
    private com.healthcare.system.repository.AdminRepository adminRepository;

    @Autowired
    private com.healthcare.system.repository.StaffRepository staffRepository;

    @Autowired
    private com.healthcare.system.repository.UserRepository userRepository;

    @Override
    public com.healthcare.system.model.Patient registerPatient(com.healthcare.system.model.Patient patient) {
        patient.setRole(com.healthcare.system.enums.Role.PATIENT);
        patient.setStatus(com.healthcare.system.enums.AccountStatus.ACTIVE);
        return patientRepository.save(patient);
    }

    @Override
    public User saveUser(User user) {
        // JPA/Hibernate will handle subclasses correctly since User is an Entity with JOINED inheritance
        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {

        System.out.println("LOGIN TRY: " + username);

        // 🔍 Check doctor
        Doctor doctor = doctorRepository.findByEmail(username);
        if (doctor != null && doctor.getStatus() == com.healthcare.system.enums.AccountStatus.ACTIVE) {
            System.out.println("Doctor found");
            if (doctor.getPassword().equals(password)) {
                return doctor;
            }
        }

        // 🔍 Check patient
        Patient patient = patientRepository.findByEmail(username);
        if (patient != null && patient.getStatus() == com.healthcare.system.enums.AccountStatus.ACTIVE) {
            System.out.println("Patient found");
            if (patient.getPassword().equals(password)) {
                return patient;
            }
        }

        // 🔍 Check admin
        com.healthcare.system.model.Admin admin = adminRepository.findByEmail(username);
        if (admin != null && admin.getStatus() == com.healthcare.system.enums.AccountStatus.ACTIVE) {
            System.out.println("Admin found");
            if (admin.getPassword().equals(password)) {
                return admin;
            }
        }

        // 🔍 Check staff
        com.healthcare.system.model.Staff staff = staffRepository.findByEmail(username);
        if (staff != null && staff.getStatus() == com.healthcare.system.enums.AccountStatus.ACTIVE) {
            System.out.println("Staff found");
            if (staff.getPassword().equals(password)) {
                return staff;
            }
        }

        System.out.println("Login failed");
        return null;
    }
}