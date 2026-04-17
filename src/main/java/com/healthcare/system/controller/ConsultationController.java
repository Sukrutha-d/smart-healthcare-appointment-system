package com.healthcare.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthcare.system.enums.AppointmentStatus;
import com.healthcare.system.model.Appointment;
import com.healthcare.system.model.MedicineItem;
import com.healthcare.system.service.AppointmentService;

@Controller
@RequestMapping("/consultation")
public class ConsultationController {

    @Autowired
    private AppointmentService appointmentService;

    // Start Consultation via Slot ID from Dashboard
    @GetMapping("/startBySlot/{slotId}")
    public String startConsultationBySlot(@PathVariable Long slotId) {
        Appointment app = appointmentService.getAppointmentBySlotId(slotId);
        if (app == null) {
            return "redirect:/doctor/dashboard";
        }
        return "redirect:/consultation/" + app.getId();
    }

    // Start Consultation and Show Form
    @GetMapping("/{appointmentId}")
    public String startConsultation(@PathVariable Long appointmentId, Model model) {
        Appointment app = appointmentService.getAppointmentById(appointmentId);
        if (app == null) {
            return "redirect:/doctor/dashboard";
        }

        // Enforce CHECKED_IN status before starting consultation
        if (app.getStatus() == AppointmentStatus.BOOKED) {
            model.addAttribute("error", "Cannot start consultation. Patient has not checked in at the front desk yet.");
            model.addAttribute("doctorName", app.getDoctorName());
            return "doctor-dashboard-error"; // We'll create a simple error page or redirect with param
        }

        // Update status to ONGOING if valid
        try {
            if (app.getStatus() == AppointmentStatus.CHECKED_IN) {
                appointmentService.updateAppointmentStatus(appointmentId, AppointmentStatus.ONGOING);
            }
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "doctor-dashboard-error";
        }

        model.addAttribute("appointment", app);
        return "consultation";
    }

    // Submit consultation details
    @PostMapping("/{appointmentId}/complete")
    public String completeConsultation(
            @PathVariable Long appointmentId,
            @RequestParam String diagnosis,
            @RequestParam(required = false) String notes,
            @RequestParam(required = false) List<String> medicineNames,
            @RequestParam(required = false) List<String> dosages,
            @RequestParam(required = false) List<Integer> durations
    ) {

        List<MedicineItem> medicines = new ArrayList<>();
        if (medicineNames != null) {
            for (int i = 0; i < medicineNames.size(); i++) {
                if (!medicineNames.get(i).trim().isEmpty()) {
                    MedicineItem item = new MedicineItem();
                    item.setMedicineName(medicineNames.get(i));
                    item.setDosage(dosages != null && dosages.size() > i ? dosages.get(i) : "");
                    item.setDurationDays(durations != null && durations.size() > i ? durations.get(i) : 0);
                    medicines.add(item);
                }
            }
        }

        try {
            appointmentService.recordVisit(appointmentId, diagnosis, notes, medicines);
        } catch (IllegalStateException e) {
            // Log or handle error
        }

        Appointment app = appointmentService.getAppointmentById(appointmentId);
        if (app != null && app.getDoctorName() != null) {
            return "redirect:/doctor/dashboard?username=" + app.getDoctorName();
        }
        
        return "redirect:/login"; // fallback
    }
}
