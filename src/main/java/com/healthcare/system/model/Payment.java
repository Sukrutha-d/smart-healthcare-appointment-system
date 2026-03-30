package com.healthcare.system.model;

import jakarta.persistence.*;
//import java.util.List;
import java.time.LocalDateTime;
import com.healthcare.system.enums.*;
@Entity
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMode mode;

    @OneToOne private Bill bill;

    public Long getId() { return id; }
    public double getAmount() { return amount; }
    public void setAmount(double a) { this.amount = a; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime p) { this.paymentDate = p; }
    public PaymentMode getMode() { return mode; }
    public void setMode(PaymentMode m) { this.mode = m; }
    public Bill getBill() { return bill; }
    public void setBill(Bill b) { this.bill = b; }
}