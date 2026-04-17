package com.healthcare.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "patient-dashboard";
    }
}
