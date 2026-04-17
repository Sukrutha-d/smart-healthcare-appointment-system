package com.healthcare.system.strategy;

import com.healthcare.system.enums.PaymentMode;
import com.healthcare.system.model.Bill;
import org.springframework.stereotype.Component;

@Component
class CashPaymentStrategy implements PaymentStrategy {
    @Override
    public void processPayment(Bill bill, double amount) {
        System.out.println("Processing Cash Payment of $" + amount + " for Bill ID: " + bill.getId());
    }

    @Override
    public PaymentMode getMode() {
        return PaymentMode.CASH;
    }
}

@Component
class CardPaymentStrategy implements PaymentStrategy {
    @Override
    public void processPayment(Bill bill, double amount) {
        System.out.println("Processing Card Payment of $" + amount + " for Bill ID: " + bill.getId());
    }

    @Override
    public PaymentMode getMode() {
        return PaymentMode.CARD;
    }
}

@Component
class UPIPaymentStrategy implements PaymentStrategy {
    @Override
    public void processPayment(Bill bill, double amount) {
        System.out.println("Processing UPI Payment of $" + amount + " for Bill ID: " + bill.getId());
    }

    @Override
    public PaymentMode getMode() {
        return PaymentMode.UPI;
    }
}
