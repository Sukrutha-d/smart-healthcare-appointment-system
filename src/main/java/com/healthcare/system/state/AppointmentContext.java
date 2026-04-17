package com.healthcare.system.state;

import com.healthcare.system.model.Appointment;
import com.healthcare.system.enums.AppointmentStatus;

public interface AppointmentContext {
    void updateStatus(Appointment appointment, AppointmentStatus newStatus);
}
