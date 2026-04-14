package com.healthcare.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.healthcare.system.model.User;
import com.healthcare.system.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password) {

        User user = userService.login(username, password);

        if (user == null) {
            return "redirect:/login?error=true";
        }

        // 🔥 IMPORTANT FIX
        if (user.getRole().name().equals("DOCTOR")) {
            return "redirect:/doctor/dashboard?username=" + user.getName();
        } else {
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}