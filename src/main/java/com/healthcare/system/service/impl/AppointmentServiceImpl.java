package com.healthcare.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.model.Appointment;
import com.healthcare.system.repository.AppointmentRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.service.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public String bookAppointment(String patientName, String doctorName, String time) {

        LocalDateTime appointmentTime = LocalDateTime.parse(time);

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
        return appointmentRepository.findAll(); // ✅ FIXED
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