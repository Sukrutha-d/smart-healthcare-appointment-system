package com.healthcare.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.healthcare.system.enums.AccountStatus;
import com.healthcare.system.enums.Role;
import com.healthcare.system.enums.SlotStatus;
import com.healthcare.system.model.AvailabilitySlot;
import com.healthcare.system.model.Doctor;
import com.healthcare.system.model.Patient;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.repository.SlotRepository;
import com.healthcare.system.repository.AdminRepository;
import com.healthcare.system.repository.StaffRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public void run(String... args) {

        System.out.println("🚀 DataLoader started...");

        // ================= DOCTOR =================
        Doctor doctor = doctorRepository.findByName("Dr. Smith");

        if (doctor == null) {
            doctor = new Doctor();
            doctor.setName("Dr. Smith");
            doctor.setEmail("doctor@test.com");
            doctor.setPassword("1234");
            doctor.setSpecialization("Cardiologist");
            doctor.setRole(Role.DOCTOR);
            doctor.setStatus(AccountStatus.ACTIVE);
            doctor = doctorRepository.save(doctor);
        }

        // ================= PATIENT =================
        Patient patient = patientRepository.findAll().stream()
                .filter(p -> p.getEmail().equals("patient@test.com"))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            patient = new Patient();
            patient.setName("John");
            patient.setEmail("patient@test.com");
            patient.setPassword("1234");
            patient.setRole(Role.PATIENT);
            patient.setStatus(AccountStatus.ACTIVE);
            patientRepository.save(patient);
        }

        // ================= ADMIN =================
        if (adminRepository.findByEmail("admin@test.com") == null) {
            com.healthcare.system.model.Admin admin = new com.healthcare.system.model.Admin();
            admin.setName("System Admin");
            admin.setEmail("admin@test.com");
            admin.setPassword("1234");
            admin.setRole(Role.ADMIN);
            admin.setStatus(AccountStatus.ACTIVE);
            adminRepository.save(admin);
        }

        // ================= STAFF =================
        if (staffRepository.findByEmail("staff@test.com") == null) {
            com.healthcare.system.model.Staff staff = new com.healthcare.system.model.Staff();
            staff.setName("Receptionist");
            staff.setEmail("staff@test.com");
            staff.setPassword("1234");
            staff.setRole(Role.STAFF);
            staff.setStatus(AccountStatus.ACTIVE);
            staff.setDepartment("Front Desk");
            staffRepository.save(staff);
        }

/* 
        // ================= SLOT GENERATION =================
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 5; i++) {

            LocalDate date = today.plusDays(i);

            System.out.println("📅 Generating slots for: " + date);

            // 🔥 MORNING 9–1
            generateSlotsIfNotExists(doctor, date.atTime(9, 0), date.atTime(13, 0));

            // 🔥 AFTERNOON 2–4
            generateSlotsIfNotExists(doctor, date.atTime(14, 0), date.atTime(16, 0));
        }
*/

        System.out.println("✅ DataLoader finished");
    }

    // ================= HELPER METHOD =================
    private void generateSlotsIfNotExists(Doctor doctor,
                                         LocalDateTime start,
                                         LocalDateTime end) {

        while (start.isBefore(end)) {

            // 🔥 PREVENT DUPLICATES
            boolean exists = slotRepository
                    .existsByDoctorAndStartTime(doctor, start);

            if (!exists) {

                AvailabilitySlot slot = new AvailabilitySlot();
                slot.setDoctor(doctor);
                slot.setStartTime(start);
                slot.setEndTime(start.plusMinutes(15));
                slot.setStatus(SlotStatus.AVAILABLE);

                slotRepository.save(slot);
            }

            start = start.plusMinutes(15);
        }
    }
}