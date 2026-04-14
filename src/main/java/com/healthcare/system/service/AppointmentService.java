package com.healthcare.system.service;

import java.time.LocalDateTime;
import java.util.List;

import com.healthcare.system.model.Appointment;
import com.healthcare.system.model.Doctor;

public interface AppointmentService {

    String bookAppointment(Long slotId, String patientName);

    List<String> getDoctorNames();

    void cancelAppointment(Long id);

    List<Appointment> getAllAppointments();

    // 🔥 ADD THIS METHOD
    void generateSlots(Doctor doctor, LocalDateTime start, LocalDateTime end);
}