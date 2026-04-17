package com.healthcare.system.controller;

import com.healthcare.system.model.Notification;
import com.healthcare.system.model.User;
import com.healthcare.system.repository.NotificationRepository;
import com.healthcare.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{username}")
    public List<Notification> getNotifications(@PathVariable String username) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getName().equalsIgnoreCase(username))
                .findFirst().orElse(null);
        
        if (user == null) return List.of();
        
        return notificationRepository.findByUserOrderByTimestampDesc(user);
    }

    @PostMapping("/read-all/{username}")
    public void markAsRead(@PathVariable String username) {
        // Simple implementation: delete or mark read. 
        // For simplicity we just return, but we could add a 'isRead' field to Notification.
    }
}
