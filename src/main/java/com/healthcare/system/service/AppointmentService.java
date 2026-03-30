package com.healthcare.system.service;

import java.util.List;
import com.healthcare.system.model.Appointment;

public interface AppointmentService {

    String bookAppointment(String patientName, String doctorName, String time);

    List<Appointment> getAllAppointments();
    List<String> getDoctorNames();
    void cancelAppointment(Long id);
}