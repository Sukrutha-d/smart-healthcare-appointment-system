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

    void generateSlots(Doctor doctor, LocalDateTime start, LocalDateTime end);

    void updateAppointmentStatus(Long id, com.healthcare.system.enums.AppointmentStatus status);

    com.healthcare.system.model.VisitRecord recordVisit(Long appointmentId, String diagnosis, String notes, List<com.healthcare.system.model.MedicineItem> medicines);

    Appointment getAppointmentById(Long id);
    Appointment getAppointmentBySlotId(Long slotId);

    List<String> getSpecializations();

    List<com.healthcare.system.model.Doctor> getDoctorsBySpecialization(String specialization);

    String rescheduleAppointment(Long appointmentId, Long newSlotId);

    void cancelSlot(Long slotId);
}