package com.healthcare.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.healthcare.system.enums.*;
@Entity
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne private User user;

    public Long getId() { return id; }
    public String getMessage() { return message; }
    public void setMessage(String m) { this.message = m; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime t) { this.timestamp = t; }
    public NotificationType getType() { return type; }
    public void setType(NotificationType t) { this.type = t; }
    public User getUser() { return user; }
    public void setUser(User u) { this.user = u; }
}