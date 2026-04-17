package com.healthcare.system.controller;

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
import com.healthcare.system.enums.PaymentMode;
import com.healthcare.system.model.Appointment;
import com.healthcare.system.model.Bill;
import com.healthcare.system.model.Staff;
import com.healthcare.system.repository.StaffRepository;
import com.healthcare.system.service.AppointmentService;
import com.healthcare.system.service.BillingService;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private BillingService billingService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam String username, Model model) {
        Staff staff = staffRepository.findByName(username);
        
        if (staff == null) {
            staff = new Staff();
            staff.setName(username);
            staff.setDepartment("General Administration");
        }

        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        List<Bill> allBills = billingService.getAllBills();

        // Populate transient billId for UI logic (Generate vs View button)
        for (Appointment app : allAppointments) {
            allBills.stream()
                .filter(b -> b.getAppointment() != null && b.getAppointment().getId().equals(app.getId()))
                .findFirst()
                .ifPresent(b -> app.setBillId(b.getId()));
        }
        
        model.addAttribute("staff", staff);
        model.addAttribute("appointments", allAppointments);
        
        return "staff-dashboard";
    }

    @PostMapping("/checkin/{id}")
    public String checkInPatient(@PathVariable Long id, @RequestParam String username) {
        appointmentService.updateAppointmentStatus(id, AppointmentStatus.CHECKED_IN);
        return "redirect:/staff/dashboard?username=" + username;
    }

    @PostMapping("/generate-bill/{appointmentId}")
    public String generateBill(@PathVariable Long appointmentId, @RequestParam String username) {
        Bill bill = billingService.generateBill(appointmentId);
        if (bill != null) {
            return "redirect:/staff/invoice/" + bill.getId() + "?username=" + username;
        }
        return "redirect:/staff/dashboard?username=" + username;
    }

    @GetMapping("/invoice/{billId}")
    public String viewInvoice(@PathVariable Long billId, @RequestParam String username, Model model) {
        Bill bill = billingService.getAllBills().stream()
                        .filter(b -> b.getId().equals(billId)).findFirst().orElse(null);
                        
        if (bill == null) {
            return "redirect:/staff/dashboard?username=" + username;
        }

        // Detect role for correct redirection back to dashboard
        String role = "STAFF";
        if (bill.getAppointment().getPatientName().equalsIgnoreCase(username)) {
            role = "PATIENT";
        }

        model.addAttribute("bill", bill);
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("paymentModes", PaymentMode.values()); 
        
        return "invoice";
    }

    @PostMapping("/pay/{billId}")
    public String processPayment(@PathVariable Long billId, @RequestParam PaymentMode mode, @RequestParam String username) {
        billingService.processPayment(billId, mode);
        return "redirect:/staff/invoice/" + billId + "?username=" + username;
    }
}
