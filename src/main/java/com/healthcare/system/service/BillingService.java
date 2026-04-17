package com.healthcare.system.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.enums.AppointmentStatus;
import com.healthcare.system.enums.BillStatus;
import com.healthcare.system.enums.PaymentMode;
import com.healthcare.system.model.Appointment;
import com.healthcare.system.model.Bill;
import com.healthcare.system.model.Payment;
import com.healthcare.system.repository.AppointmentRepository;
import com.healthcare.system.repository.BillRepository;
import com.healthcare.system.repository.PaymentRepository;
import com.healthcare.system.strategy.PaymentStrategy;

@Service
public class BillingService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private List<PaymentStrategy> paymentStrategies;

    /**
     * Generate a bill for an appointment. 
     */
    public Bill generateBill(Long appointmentId) {
        Appointment app = appointmentRepository.findById(appointmentId).orElse(null);
        if (app == null || app.getStatus() != AppointmentStatus.COMPLETED) {
            return null; 
        }

        List<Bill> existing = billRepository.findAll().stream()
                .filter(b -> b.getAppointment() != null && b.getAppointment().getId().equals(appointmentId))
                .toList();
        
        if (!existing.isEmpty()) {
            return existing.get(0);
        }

        Bill bill = new Bill();
        bill.setAppointment(app);
        bill.setStatus(BillStatus.PENDING);
        bill.setTotalAmount(500.0 + (Math.floor(Math.random() * 200))); 

        return billRepository.save(bill);
    }

    public Payment processPayment(Long billId, PaymentMode mode) {
        Bill bill = billRepository.findById(billId).orElse(null);
        if (bill == null || bill.getStatus() == BillStatus.PAID) {
            return null;
        }

        // ✅ STRATEGY PATTERN
        PaymentStrategy strategy = paymentStrategies.stream()
                .filter(s -> s.getMode() == mode)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment strategy not found for " + mode));

        strategy.processPayment(bill, bill.getTotalAmount());

        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setAmount(bill.getTotalAmount());
        payment.setMode(mode);
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        bill.setStatus(BillStatus.PAID);
        bill.setPayment(savedPayment);
        billRepository.save(bill);

        return savedPayment;
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
}
