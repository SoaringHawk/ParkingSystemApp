package  com.example.parkingreservationapp.PaymentAdapterPackage;

public class PaymentAdapter implements PaymentProcessor{
    GlobalPayments Payment;

    public PaymentAdapter(String type){
        if (type == "cash"){
            Payment = new CashPayment();
        }else{
            Payment = new CreditCardPayment();
        }
    }

    @Override
    public void processPayment(double amount) {
        Payment.makePayment(amount);
    }
}
