package com.healthcare.system.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.healthcare.system.model.AvailabilitySlot;
import com.healthcare.system.model.Doctor;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.SlotRepository;
import com.healthcare.system.service.AppointmentService;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private AppointmentService appointmentService;

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam String username,
            @RequestParam(required = false) String date,
            Model model) {

        Doctor doctor = doctorRepository.findByName(username);

        if (doctor == null) {
            return "redirect:/login";
        }

        LocalDate selectedDate;

        if (date == null || date.isEmpty()) {
            selectedDate = LocalDate.now();
        } else {
            selectedDate = LocalDate.parse(date);
        }

        // 🔥 FETCH ALL SLOTS
        List<AvailabilitySlot> allSlots =
                slotRepository.findByDoctorOrderByStartTimeAsc(doctor);

        // 🔥 FILTER BY DATE
        List<AvailabilitySlot> filteredSlots = allSlots.stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(selectedDate))
                .toList();

        // 🔥 POPULATE APPOINTMENT STATUS
        for (AvailabilitySlot slot : filteredSlots) {
            com.healthcare.system.model.Appointment app = appointmentService.getAppointmentBySlotId(slot.getId());
            if (app != null) {
                slot.setAppointmentStatus(app.getStatus());
            }
        }

        model.addAttribute("slots", filteredSlots);
        model.addAttribute("slotsExist", !filteredSlots.isEmpty());
        model.addAttribute("doctorName", doctor.getName());
        model.addAttribute("selectedDate", selectedDate.toString());

        return "doctor-dashboard";
    }

    @PostMapping("/generate-slots")
    public String generateSlots(
            @RequestParam String username,
            @RequestParam String date,
            @RequestParam String startTime,
            @RequestParam String endTime,
            Model model) {

        Doctor doctor = doctorRepository.findByName(username);
        if (doctor == null) return "redirect:/login";

        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime start = localDate.atTime(java.time.LocalTime.parse(startTime));
        LocalDateTime end = localDate.atTime(java.time.LocalTime.parse(endTime));

        if (start.isBefore(LocalDateTime.now())) {
            // Error handling could be added here, simplified for now
        }

        appointmentService.generateSlots(doctor, start, end);

        return "redirect:/doctor/dashboard?username=" + username + "&date=" + date;
    }

    @PostMapping("/cancel-slot/{id}")
    public String cancelSlot(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam String date) {

        appointmentService.cancelSlot(id);

        return "redirect:/doctor/dashboard?username=" + username + "&date=" + date;
    }
}