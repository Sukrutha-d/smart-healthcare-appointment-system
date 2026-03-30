package com.healthcare.system.controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.healthcare.system.dto.ApiResponse;
import com.healthcare.system.model.Appointment;
import com.healthcare.system.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // ✅ BOOK APPOINTMENT
    @PostMapping("/book")
    public String book(@RequestParam String patientName,
                       @RequestParam String doctorName,
                       @RequestParam String time,
                        Model model) {

        String result = appointmentService.bookAppointment(patientName, doctorName, time);

    // ❌ if slot taken → stay on same page
        if (result.contains("already booked")) {
            model.addAttribute("error", result);
            return "book";
        }

    // ✅ success → redirect to view page
        return "redirect:/view";
    }
    @GetMapping("/view")
    public String viewPage() {
        return "view";
    }
    // ✅ GET DOCTORS
    @GetMapping("/doctors")
    public List<String> getDoctors() {
        return appointmentService.getDoctorNames();
    }

    // ✅ CANCEL
    @PutMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return "Appointment cancelled";
    }

    // ✅ GET ALL
    @GetMapping("/all")
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