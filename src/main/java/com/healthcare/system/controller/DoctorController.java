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

        // 🔥 AUTO GENERATE IF EMPTY
        if (filteredSlots.isEmpty()) {

            LocalDateTime start = selectedDate.atTime(9, 0);
            LocalDateTime end = selectedDate.atTime(16, 0);

            appointmentService.generateSlots(doctor, start, end);

            // reload
            allSlots = slotRepository.findByDoctorOrderByStartTimeAsc(doctor);

            filteredSlots = allSlots.stream()
                    .filter(s -> s.getStartTime().toLocalDate().equals(selectedDate))
                    .toList();
        }

        model.addAttribute("slots", filteredSlots);
        model.addAttribute("doctorName", doctor.getName());
        model.addAttribute("selectedDate", selectedDate.toString());

        return "doctor-dashboard";
    }
}