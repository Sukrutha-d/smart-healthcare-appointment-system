package com.healthcare.system.model;
import jakarta.persistence.*;
//import java.time.LocalDateTime;
import com.healthcare.system.enums.*;
@Entity
public class Bill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @OneToOne private Appointment appointment;
    @OneToOne(mappedBy = "bill") private Payment payment;

    public Long getId() { return id; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double t) { this.totalAmount = t; }
    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus s) { this.status = s; }
    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment a) { this.appointment = a; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment p) { this.payment = p; }
}