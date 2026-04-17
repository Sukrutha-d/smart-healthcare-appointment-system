package com.healthcare.system.service.impl;

import com.healthcare.system.enums.NotificationType;
import com.healthcare.system.model.Appointment;
import com.healthcare.system.model.Notification;
import com.healthcare.system.model.User;
import com.healthcare.system.observer.NotificationObserver;
import com.healthcare.system.repository.NotificationRepository;
import com.healthcare.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationObserver {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAppointmentChanged(Appointment appointment, String message, NotificationType category) {
        // Send to Patient
        User patient = userRepository.findAll().stream()
                .filter(u -> u.getName().equalsIgnoreCase(appointment.getPatientName()))
                .findFirst().orElse(null);
        
        if (patient != null) {
            createNotification(patient, message, category);
        }

        // Send to Doctor
        if (appointment.getSlot() != null && appointment.getSlot().getDoctor() != null) {
            User doctor = appointment.getSlot().getDoctor();
            createNotification(doctor, message, category);
        }
    }

    private void createNotification(User user, String message, NotificationType category) {
        Notification n = new Notification();
        n.setUser(user);
        n.setMessage(message);
        n.setType(category);
        n.setTimestamp(LocalDateTime.now());
        notificationRepository.save(n);
        
        // Mock actual sending (e.g. Email/SMS)
        System.out.println("[NOTIFICATION] To: " + user.getName() + " | Category: " + category + " | Msg: " + message);
    }
}
