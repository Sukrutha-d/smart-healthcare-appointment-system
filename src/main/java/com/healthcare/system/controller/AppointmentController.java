package com.healthcare.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.healthcare.system.dto.ApiResponse;
import com.healthcare.system.model.Appointment;
import com.healthcare.system.service.AppointmentService;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // =========================
    // ✅ PAGE NAVIGATION
    // =========================

    @GetMapping("/view")
    public String viewPage() {
        return "view";
    }

    @GetMapping("/book")
    public String bookPage() {
        return "book";
    }

    // =========================
    // ✅ BOOK APPOINTMENT (FIXED)
    // =========================

    @PostMapping("/book")
    @ResponseBody
    public String book(
            @RequestParam String patientName,
            @RequestParam Long slotId) {

        return appointmentService.bookAppointment(slotId, patientName);
    }

    @GetMapping("/patient/{name}")
@ResponseBody
public List<Appointment> getPatientAppointments(@PathVariable String name) {
    return appointmentService.getAllAppointments()
            .stream()
            .filter(a -> a.getPatientName().equalsIgnoreCase(name))
            .toList();
}

    // =========================
    // ✅ GET DOCTORS
    // =========================

    @GetMapping("/doctors")
    @ResponseBody
    public List<String> getDoctors() {
        return appointmentService.getDoctorNames();
    }

    // =========================
    // ✅ CANCEL APPOINTMENT
    // =========================

    @PutMapping("/cancel/{id}")
    @ResponseBody
    public String cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return "Appointment cancelled";
    }

    // =========================
    // ✅ GET ALL APPOINTMENTS
    // =========================

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<Appointment>>> getAllAppointments() {

        List<Appointment> list = appointmentService.getAllAppointments();

        if (list.isEmpty()) {
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "No appointments found", list)
            );
        }

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Appointments fetched successfully", list)
        );
    }
}