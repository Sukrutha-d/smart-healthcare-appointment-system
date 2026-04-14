package com.healthcare.system.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.enums.SlotStatus;
import com.healthcare.system.model.Appointment;
import com.healthcare.system.model.AvailabilitySlot;
import com.healthcare.system.model.Doctor;
import com.healthcare.system.repository.AppointmentRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.SlotRepository;
import com.healthcare.system.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SlotRepository slotRepository;

    // ================= BOOK =================
    @Override
    public String bookAppointment(Long slotId, String patientName) {

        AvailabilitySlot slot = slotRepository.findById(slotId).orElse(null);

        if (slot == null) return "Slot not found ❌";

        if (slot.getStatus() == SlotStatus.BOOKED) {
            return "Slot already booked ❌";
        }

        // ✅ BLOCK PAST SLOT
        if (slot.getStartTime().isBefore(LocalDateTime.now())) {
            return "Cannot book past slot ❌";
        }

        // 🔥 CREATE APPOINTMENT
        Appointment a = new Appointment();
        a.setPatientName(patientName);
        a.setDoctorName(slot.getDoctor().getName());
        a.setAppointmentTime(slot.getStartTime());
        a.setStatus(SlotStatus.BOOKED);

        // 🔥 VERY IMPORTANT FIX
        a.setSlot(slot);

        // 🔥 UPDATE SLOT
        slot.setStatus(SlotStatus.BOOKED);
        slot.setPatientName(patientName);

        slotRepository.save(slot);
        appointmentRepository.save(a);

        return "Appointment booked successfully ✅";
    }

    // ================= CANCEL =================
    @Override
    public void cancelAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        AvailabilitySlot slot = appointment.getSlot();

        if (slot != null) {
            slot.setStatus(SlotStatus.AVAILABLE);
            slot.setPatientName(null);
            slotRepository.save(slot);
        }

        appointmentRepository.delete(appointment);
    }

    // ================= GET =================
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<String> getDoctorNames() {
        return doctorRepository.findAll()
                .stream()
                .map(d -> d.getName())
                .toList();
    }

    // ================= GENERATE =================
    @Override
    public void generateSlots(Doctor doctor, LocalDateTime start, LocalDateTime end) {

        while (start.isBefore(end)) {

            AvailabilitySlot slot = new AvailabilitySlot();
            slot.setDoctor(doctor);
            slot.setStartTime(start);
            slot.setEndTime(start.plusMinutes(15));
            slot.setStatus(SlotStatus.AVAILABLE);

            slotRepository.save(slot);

            start = start.plusMinutes(15);
        }
    }
}