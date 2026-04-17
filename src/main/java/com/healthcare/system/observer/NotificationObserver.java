package com.healthcare.system.observer;

import com.healthcare.system.model.Appointment;
import com.healthcare.system.enums.NotificationType;

public interface NotificationObserver {
    void onAppointmentChanged(Appointment appointment, String message, NotificationType category);
}
