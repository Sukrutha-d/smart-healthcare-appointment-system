package com.healthcare.system.strategy;

import com.healthcare.system.model.Bill;
import com.healthcare.system.enums.PaymentMode;

public interface PaymentStrategy {
    void processPayment(Bill bill, double amount);
    PaymentMode getMode();
}
