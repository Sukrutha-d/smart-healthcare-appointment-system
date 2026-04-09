package com.healthcare.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // 🔥 CRITICAL IMPORT
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.healthcare.system.dto.ApiResponse;
import com.healthcare.system.model.Appointment;
import com.healthcare.system.service.AppointmentService;

@Controller // 🎯 Must be @Controller to load HTML files
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // =========================
    // ✅ PAGE NAVIGATION
    // =========================

    @GetMapping("/view")
    public String viewPage() {
        return "view"; // Looks for src/main/resources/templates/view.html
    }

    @GetMapping("/book")
    public String bookPage() {
        return "book"; // Looks for src/main/resources/templates/book.html
    }

    // =========================
    // ✅ BOOK APPOINTMENT (API)
    // =========================

    @PostMapping("/book")
    @ResponseBody
    public String book(@RequestBody Map<String, String> payload) {
        String patientName = payload.get("patientName");
        String doctorName = payload.get("doctorName");
        String time = payload.get("appointmentTime"); 

        return appointmentService.bookAppointment(patientName, doctorName, time);
    }

    // =========================
    // ✅ GET DOCTOR LIST (API)
    // =========================

    @GetMapping("/doctors")
    @ResponseBody
    public List<String> getDoctors() {
        return appointmentService.getDoctorNames();
    }

    // =========================
    // ✅ CANCEL APPOINTMENT (API)
    // =========================

    @PutMapping("/cancel/{id}")
    @ResponseBody
    public String cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return "Appointment cancelled";
    }
    @GetMapping("/")
    public String home() {
    return "index"; // This looks for templates/index.html
}
    // =========================
    // ✅ GET ALL APPOINTMENTS (API)
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