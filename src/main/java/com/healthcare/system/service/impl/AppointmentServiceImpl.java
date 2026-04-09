package com.healthcare.system.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.model.Appointment;
import com.healthcare.system.model.Doctor;
import com.healthcare.system.repository.AppointmentRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public String bookAppointment(String patientName, String doctorName, String time) {
        // ✅ Fix: HTML5 datetime-local uses 'T' (e.g., 2026-04-08T10:30)
        // LocalDateTime.parse(time) handles this perfectly.
        LocalDateTime appointmentTime = LocalDateTime.parse(time);

        Doctor doctor = doctorRepository.findByName(doctorName);
        if (doctor == null) {
            return "Doctor not found ❌";
        }

        if (doctor.getAvailableFrom() == null || doctor.getAvailableTo() == null) {
            return "Doctor schedule not set ❌";
        }

        LocalTime appointmentLocalTime = appointmentTime.toLocalTime();
        LocalTime start = LocalTime.parse(doctor.getAvailableFrom());
        LocalTime end = LocalTime.parse(doctor.getAvailableTo());

        if (appointmentLocalTime.isBefore(start) || appointmentLocalTime.isAfter(end)) {
            return "Doctor not available ⛔ (Available: " + start + " - " + end + ")";
        }

        boolean exists = appointmentRepository
                .existsByDoctorNameAndAppointmentTime(doctorName, appointmentTime);

        if (exists) {
            return "Doctor already booked at this time ❌";
        }

        Appointment a = new Appointment();
        a.setPatientName(patientName);
        a.setDoctorName(doctorName);
        a.setAppointmentTime(appointmentTime);

        appointmentRepository.save(a);
        return "Appointment booked successfully ✅";
    }

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

    @Override
    public void cancelAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}