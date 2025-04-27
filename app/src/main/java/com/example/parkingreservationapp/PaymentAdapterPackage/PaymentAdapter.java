package com.example.parkingreservationapp.PaymentAdapterPackage;

import android.app.Activity;

public class PaymentAdapter implements PaymentProcessor {
    private GlobalPayments payment;

    public PaymentAdapter(String type, Activity activity,
                          CreditCardPayment.PaymentResultListener listener) {
        if (type.equalsIgnoreCase("cash")) {
            payment = new CashPayment();
        } else {
            payment = new CreditCardPayment(activity, listener);
        }
    }

    @Override
    public boolean processPayment(double amount) {
        payment.makePayment(amount);
        return true;
    }
}