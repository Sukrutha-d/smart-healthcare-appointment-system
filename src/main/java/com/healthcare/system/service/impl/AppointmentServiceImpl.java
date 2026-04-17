package com.healthcare.system.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.enums.SlotStatus;
import com.healthcare.system.enums.NotificationType;
import com.healthcare.system.model.Appointment;
import com.healthcare.system.model.AvailabilitySlot;
import com.healthcare.system.model.Doctor;
import com.healthcare.system.repository.AppointmentRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.SlotRepository;
import com.healthcare.system.service.AppointmentService;
import com.healthcare.system.observer.NotificationObserver;
import com.healthcare.system.state.AppointmentContext;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private com.healthcare.system.repository.VisitRecordRepository visitRecordRepository;

    @Autowired
    private com.healthcare.system.repository.PrescriptionRepository prescriptionRepository;

    @Autowired
    private com.healthcare.system.repository.PatientRepository patientRepository;

    @Autowired
    private NotificationObserver notificationObserver;

    @Autowired
    private AppointmentContext appointmentContext;

    // ================= BOOK =================
    @Override
    public String bookAppointment(Long slotId, String patientName) {

        AvailabilitySlot slot = slotRepository.findById(slotId).orElse(null);

        if (slot == null) return "Slot not found ❌";

        if (slot.getStatus() == SlotStatus.BOOKED) {
            return "Slot already booked ❌";
        }

        // ✅ BLOCK PAST SLOT (Allow immediate booking for current day testing)
        LocalDateTime now = LocalDateTime.now();
        if (slot.getStartTime().isBefore(now)) {
            return "Cannot book slots that have already started ❌";
        }

        // 🔥 CREATE APPOINTMENT
        Appointment a = new Appointment();
        a.setPatientName(patientName);
        a.setDoctorName(slot.getDoctor().getName());
        a.setAppointmentTime(slot.getStartTime());
        a.setStatus(com.healthcare.system.enums.AppointmentStatus.BOOKED);

        // 🔥 VERY IMPORTANT FIX
        a.setSlot(slot);

        // 🔥 UPDATE SLOT
        slot.setStatus(SlotStatus.BOOKED);
        slot.setPatientName(patientName);

        slotRepository.save(slot);
        appointmentRepository.save(a);

        // ✅ OBSERVER NOTIFY
        notificationObserver.onAppointmentChanged(a, "Confirmed: Appointment booked for " + a.getAppointmentTime() + " with Dr. " + a.getDoctorName(), NotificationType.CONFIRMATION);

        return "Appointment booked successfully ✅";
    }

    // ================= CANCEL =================
    @Override
    public void cancelAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        AvailabilitySlot slot = appointment.getSlot();

        if (slot != null) {
            slot.setStatus(SlotStatus.AVAILABLE);
            slot.setPatientName(null);
            slotRepository.save(slot);
        }

        // ✅ OBSERVER NOTIFY
        notificationObserver.onAppointmentChanged(appointment, "Cancelled: Appointment with Dr. " + appointment.getDoctorName() + " on " + appointment.getAppointmentTime() + " has been cancelled.", NotificationType.CANCELLATION);

        appointmentRepository.delete(appointment);
    }

    // ================= GET =================
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<String> getDoctorNames() {
        return doctorRepository.findAll()
                .stream()
                .filter(d -> d.getStatus() == com.healthcare.system.enums.AccountStatus.ACTIVE)
                .map(d -> d.getName())
                .toList();
    }

    // ================= GENERATE =================
    @Override
    public void generateSlots(Doctor doctor, LocalDateTime start, LocalDateTime end) {
        if (doctor.getStatus() != com.healthcare.system.enums.AccountStatus.ACTIVE) return;

        while (start.isBefore(end)) {

            // 🔥 PREVENT DUPLICATES
            if (!slotRepository.existsByDoctorAndStartTime(doctor, start)) {
                AvailabilitySlot slot = new AvailabilitySlot();
                slot.setDoctor(doctor);
                slot.setStartTime(start);
                slot.setEndTime(start.plusMinutes(15));
                slot.setStatus(SlotStatus.AVAILABLE);

                slotRepository.save(slot);
            }

            start = start.plusMinutes(15);
        }
    }

    // ================= CONSULTATION =================
    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    @Override
    public Appointment getAppointmentBySlotId(Long slotId) {
        return appointmentRepository.findAll().stream()
                .filter(a -> a.getSlot() != null && a.getSlot().getId().equals(slotId))
                .findFirst().orElse(null);
    }

    @Override
    public void updateAppointmentStatus(Long id, com.healthcare.system.enums.AppointmentStatus status) {
        Appointment app = appointmentRepository.findById(id).orElse(null);
        if (app != null) {
            // ✅ STATE PATTERN
            appointmentContext.updateStatus(app, status);
            appointmentRepository.save(app);
        }
    }

    @Override
    public com.healthcare.system.model.VisitRecord recordVisit(Long appointmentId, String diagnosis, String notes, List<com.healthcare.system.model.MedicineItem> medicines) {
        Appointment app = appointmentRepository.findById(appointmentId).orElse(null);
        if (app == null) return null;

        com.healthcare.system.model.VisitRecord visit = new com.healthcare.system.model.VisitRecord();
        visit.setVisitDate(LocalDateTime.now());
        visit.setDiagnosis(diagnosis);
        visit.setDoctor(app.getSlot().getDoctor());
        
        com.healthcare.system.model.Patient patient = patientRepository.findByName(app.getPatientName());
        visit.setPatient(patient);

        com.healthcare.system.model.VisitRecord savedVisit = visitRecordRepository.save(visit);

        if (notes != null || (medicines != null && !medicines.isEmpty())) {
            com.healthcare.system.model.Prescription prescription = new com.healthcare.system.model.Prescription();
            prescription.setNotes(notes);
            prescription.setVisitRecord(savedVisit);
            
            if (medicines != null) {
                for (com.healthcare.system.model.MedicineItem m : medicines) {
                    m.setPrescription(prescription);
                }
                prescription.setMedicines(medicines);
            }
            
            prescriptionRepository.save(prescription);
        }

        // Mark as completed
        updateAppointmentStatus(app.getId(), com.healthcare.system.enums.AppointmentStatus.COMPLETED);

        return savedVisit;
    }

    @Override
    public List<String> getSpecializations() {
        return doctorRepository.findAll().stream()
                .filter(d -> d.getStatus() == com.healthcare.system.enums.AccountStatus.ACTIVE)
                .map(Doctor::getSpecialization)
                .filter(s -> s != null && !s.isEmpty())
                .distinct()
                .toList();
    }

    @Override
    public List<com.healthcare.system.model.Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization).stream()
                .filter(d -> d.getStatus() == com.healthcare.system.enums.AccountStatus.ACTIVE)
                .toList();
    }

    @Override
    public String rescheduleAppointment(Long appointmentId, Long newSlotId) {
        Appointment app = appointmentRepository.findById(appointmentId).orElse(null);
        if (app == null) return "Appointment not found ❌";

        AvailabilitySlot oldSlot = app.getSlot();
        AvailabilitySlot newSlot = slotRepository.findById(newSlotId).orElse(null);

        if (newSlot == null) return "New slot not found ❌";
        if (newSlot.getStatus() == SlotStatus.BOOKED) return "New slot already booked ❌";

        // Update old slot status back to AVAILABLE
        if (oldSlot != null) {
            oldSlot.setStatus(SlotStatus.AVAILABLE);
            oldSlot.setPatientName(null);
            slotRepository.save(oldSlot);
        }

        // Update new slot status to BOOKED
        newSlot.setStatus(SlotStatus.BOOKED);
        newSlot.setPatientName(app.getPatientName());
        slotRepository.save(newSlot);

        // Update appointment details
        app.setSlot(newSlot);
        app.setAppointmentTime(newSlot.getStartTime());
        app.setDoctorName(newSlot.getDoctor().getName());
        appointmentRepository.save(app);

        // ✅ OBSERVER NOTIFY
        notificationObserver.onAppointmentChanged(app, "Rescheduled: Your appointment is now with Dr. " + app.getDoctorName() + " on " + app.getAppointmentTime(), NotificationType.RESCHEDULING);

        return "Appointment rescheduled successfully ✅";
    }

    @Override
    public void cancelSlot(Long slotId) {
        AvailabilitySlot slot = slotRepository.findById(slotId).orElse(null);
        if (slot == null) return;

        // If booked, cancel the associated appointment first
        if (slot.getStatus() == SlotStatus.BOOKED) {
            Appointment app = getAppointmentBySlotId(slotId);
            if (app != null) {
                // Use state manager to mark as CANCELLED
                appointmentContext.updateStatus(app, com.healthcare.system.enums.AppointmentStatus.CANCELLED);
                appointmentRepository.save(app);
                
                // Notify
                notificationObserver.onAppointmentChanged(app, 
                    "Urgent: Dr. " + app.getDoctorName() + " has cancelled the scheduled slot on " + app.getAppointmentTime() + " due to unforeseen circumstances.", 
                    NotificationType.CANCELLATION);
            }
        }

        // Delete the slot
        slotRepository.delete(slot);
    }
}