package com.healthcare.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.healthcare.system.enums.Role;
import com.healthcare.system.enums.SlotStatus;
import com.healthcare.system.model.AvailabilitySlot;
import com.healthcare.system.model.Doctor;
import com.healthcare.system.model.Patient;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.repository.SlotRepository;

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
            doctor = doctorRepository.save(doctor);
        }

        // ================= PATIENT =================
        Patient patient = patientRepository.findAll().stream()
                .filter(p -> p.getEmail().equals("patient@test.com"))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            patient = new Patient();
            patient.setName("Sanjana");
            patient.setEmail("patient@test.com");
            patient.setPassword("1234");
            patient.setRole(Role.PATIENT);
            patientRepository.save(patient);
        }

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

        System.out.println("✅ DataLoader finished");
    }

    // ================= HELPER METHOD =================
    private void generateSlotsIfNotExists(Doctor doctor,
                                         LocalDateTime start,
                                         LocalDateTime end) {

        while (start.isBefore(end)) {

            // 🔥 PREVENT DUPLICATES
            // boolean exists = slotRepository
            //         .existsByDoctorAndStartTime(doctor, start);

            // if (!exists) {

                AvailabilitySlot slot = new AvailabilitySlot();
                slot.setDoctor(doctor);
                slot.setStartTime(start);
                slot.setEndTime(start.plusMinutes(15));
                slot.setStatus(SlotStatus.AVAILABLE);

                slotRepository.save(slot);
            // }

            start = start.plusMinutes(15);
        }
    }
}