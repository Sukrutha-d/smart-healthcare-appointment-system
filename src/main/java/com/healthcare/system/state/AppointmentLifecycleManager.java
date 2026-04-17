package com.healthcare.system.state;

import com.healthcare.system.enums.AppointmentStatus;
import com.healthcare.system.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentLifecycleManager implements AppointmentContext {

    @Override
    public void updateStatus(Appointment appointment, AppointmentStatus newStatus) {
        AppointmentStatus current = appointment.getStatus();
        
        // Prevent transitions from terminal states
        if (current == AppointmentStatus.COMPLETED || current == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot change status of an appointment that is already " + current);
        }

        switch (newStatus) {
            case BOOKED:
                // Typically used for initial booking or rescheduling (reset)
                break;
                
            case CHECKED_IN:
                if (current != AppointmentStatus.BOOKED) {
                    throw new IllegalStateException("Can only check-in from BOOKED status");
                }
                break;
                
            case ONGOING:
                if (current != AppointmentStatus.CHECKED_IN) {
                    throw new IllegalStateException("Consultation can only start after patient is CHECKED_IN. Current status: " + current);
                }
                break;
                
            case COMPLETED:
                // Can complete from CHECKED_IN (direct) or ONGOING (normal flow)
                if (current != AppointmentStatus.CHECKED_IN && current != AppointmentStatus.ONGOING) {
                    throw new IllegalStateException("Can only complete from CHECKED_IN or ONGOING status");
                }
                break;
                
            case CANCELLED:
                // Can cancel anytime unless completed
                break;
                
            default:
                break;
        }
        
        appointment.setStatus(newStatus);
    }
}
