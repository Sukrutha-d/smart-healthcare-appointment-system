package com.healthcare.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.healthcare.system.model.Patient;
import com.healthcare.system.service.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private com.healthcare.system.factory.UserFactory userFactory;

    @GetMapping("/register")
    public String registrationPage(Model model) {
        model.addAttribute("patient", new Patient());
        return "registration";
    }

    @PostMapping("/register")
    public String registerPatient(@ModelAttribute com.healthcare.system.model.Patient patient) {
        // Use factory for consistency, though ModelAttribute already created the object
        // We can just set fields if needed, but to strictly use the pattern:
        com.healthcare.system.model.User user = userFactory.createUser(
            com.healthcare.system.enums.Role.PATIENT, 
            patient.getName(), 
            patient.getEmail(), 
            patient.getPassword(),
            null
        );
        userService.saveUser(user);
        return "redirect:/login?registered=true";
    }
}
