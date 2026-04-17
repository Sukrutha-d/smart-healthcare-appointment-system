package com.healthcare.system.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.enums.AccountStatus;
import com.healthcare.system.enums.BillStatus;
import com.healthcare.system.enums.Role;
import com.healthcare.system.model.Bill;
import com.healthcare.system.model.Doctor;
import com.healthcare.system.model.Patient;
import com.healthcare.system.model.Staff;
import com.healthcare.system.model.User;
import com.healthcare.system.repository.BillRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.repository.StaffRepository;
import com.healthcare.system.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BillRepository billRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() != Role.ADMIN) // Exclude Admin
                .filter(u -> u.getStatus() != AccountStatus.DELETED) // Exclude Deleted
                .sorted((u1, u2) -> {
                    int roleComp = u1.getRole().compareTo(u2.getRole());
                    if (roleComp != 0) return roleComp;
                    return u1.getName().compareToIgnoreCase(u2.getName());
                })
                .toList();
    }

    public void updateUserStatus(Long userId, AccountStatus status) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setStatus(status);
            userRepository.save(user);
        }
    }

    public void deleteUser(Long userId) {
        System.out.println("SOFT DELETE TRIGGERED FOR USER ID: " + userId);
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            System.out.println("Marking user '" + user.getName() + "' as DELETED");
            user.setStatus(AccountStatus.DELETED);
            userRepository.save(user);
        }
    }

    public void updateUser(Long id, String name, String email, String extraData) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;

        user.setName(name);
        user.setEmail(email);

        if (user instanceof Doctor) {
            ((Doctor) user).setSpecialization(extraData);
        } else if (user instanceof Staff) {
            ((Staff) user).setDepartment(extraData);
        }

        userRepository.save(user);
    }

    public Map<String, Object> generateSystemReport() {
        Map<String, Object> report = new HashMap<>();
        
        long totalUsers = userRepository.count();
        long totalDoctors = doctorRepository.count();
        long totalPatients = patientRepository.count();
        
        List<Bill> allBills = billRepository.findAll();
        double totalRevenue = allBills.stream()
                .filter(b -> b.getStatus() == BillStatus.PAID)
                .mapToDouble(Bill::getTotalAmount)
                .sum();

        long todayAppointments = allBills.stream().filter(b -> 
            b.getAppointment() != null && 
            b.getAppointment().getAppointmentTime() != null &&
            b.getAppointment().getAppointmentTime().toLocalDate().equals(LocalDate.now())
        ).count();

        // Weekly Revenue (Simple sum)
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        double weeklyRevenue = allBills.stream()
                .filter(b -> b.getStatus() == BillStatus.PAID && b.getAppointment() != null && b.getAppointment().getAppointmentTime().toLocalDate().isAfter(weekAgo))
                .mapToDouble(Bill::getTotalAmount)
                .sum();

        // Doctor Performance (Appointments per doctor)
        Map<String, Long> doctorStats = allBills.stream()
                .filter(b -> b.getAppointment() != null)
                .collect(Collectors.groupingBy(b -> b.getAppointment().getDoctorName(), Collectors.counting()));

        report.put("totalUsers", totalUsers);
        report.put("totalDoctors", totalDoctors);
        report.put("totalPatients", totalPatients);
        report.put("totalRevenue", totalRevenue);
        report.put("weeklyRevenue", weeklyRevenue);
        report.put("todayAppointments", todayAppointments);
        report.put("doctorStats", doctorStats);

        return report;
    }
}
