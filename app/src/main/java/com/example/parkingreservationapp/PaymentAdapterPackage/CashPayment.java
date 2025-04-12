package PaymentAdapterPackage;

public class CashPayment extends GlobalPayments{
    @Override
    public void makePayment(double amount) {
        System.out.println("Payment of $" + amount + " in cash.");
    }


}
