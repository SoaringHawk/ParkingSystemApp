package PaymentAdapterPackage;

public class CreditCardPayment extends GlobalPayments{
    public void makePayment(double amount) {
        System.out.println("Payment of $" + amount + " with Credit Card.");
    }
}
