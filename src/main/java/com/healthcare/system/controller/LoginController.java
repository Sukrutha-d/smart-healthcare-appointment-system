package com.healthcare.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
// Import your service here (adjust package name as needed)
import com.healthcare.system.service.UserService; 

@Controller
public class LoginController {

    @Autowired
    private UserService userService; // 🔥 CRITICAL: Don't forget this!

    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // This calls your database logic
        boolean isValid = userService.validate(username, password);

        if (isValid) {
            return "redirect:/dashboard"; 
        } else {
            // Sends them back to login with an error flag
            return "redirect:/login?error=true"; 
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; 
    }
}