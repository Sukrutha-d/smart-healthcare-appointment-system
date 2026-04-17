package com.healthcare.system.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthcare.system.enums.AccountStatus;
import com.healthcare.system.model.Admin;
import com.healthcare.system.repository.AdminRepository;
import com.healthcare.system.service.AdminService;
import com.healthcare.system.service.UserService;
import com.healthcare.system.model.User;
import com.healthcare.system.enums.Role;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private com.healthcare.system.factory.UserFactory userFactory;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam String username, Model model) {
        Admin admin = adminRepository.findByName(username);
        
        if (admin == null) {
            admin = new Admin();
            admin.setName(username);
        }

        Map<String, Object> report = adminService.generateSystemReport();
        
        model.addAttribute("admin", admin);
        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("report", report);
        
        return "admin-dashboard";
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id, @RequestParam String username) {
        adminService.updateUserStatus(id, AccountStatus.ACTIVE);
        return "redirect:/admin/dashboard?username=" + username;
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id, @RequestParam String username) {
        adminService.updateUserStatus(id, AccountStatus.INACTIVE);
        return "redirect:/admin/dashboard?username=" + username;
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, @RequestParam String username) {
        adminService.deleteUser(id);
        return "redirect:/admin/dashboard?username=" + username;
    }

    @PostMapping("/users/create")
    public String createUser(
            @RequestParam String name, 
            @RequestParam String email, 
            @RequestParam String password, 
            @RequestParam Role role,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String department,
            @RequestParam String adminUser) {
        
        String extraData = (role == Role.DOCTOR) ? specialization : department;
        User user = userFactory.createUser(role, name, email, password, extraData);
        
        userService.saveUser(user);
        
        // Handle redirect based on where we want to land
        String section = "overview";
        if (role == Role.DOCTOR) section = "doctors";
        else if (role == Role.STAFF) section = "staff";
        
        return "redirect:/admin/dashboard?username=" + adminUser + "#" + section;
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String extraData,
            @RequestParam String adminUser,
            @RequestParam String section) {
        
        adminService.updateUser(id, name, email, extraData);
        return "redirect:/admin/dashboard?username=" + adminUser + "#" + section;
    }
}
