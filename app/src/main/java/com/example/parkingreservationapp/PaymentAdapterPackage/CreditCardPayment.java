package  com.example.parkingreservationapp.PaymentAdapterPackage;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CreditCardPayment extends GlobalPayments {
    private final Activity activity;
    private final PaymentResultListener listener;

    public interface PaymentResultListener {
        void onPaymentSuccess();
        void onPaymentCanceled();
        void onPaymentError(String error);
    }

    public CreditCardPayment(Activity activity, PaymentResultListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public void makePayment(double amount) {
        showCreditCardDialog(amount);
    }

    private void showCreditCardDialog(double amount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Enter Credit Card Details");
        builder.setMessage("Total amount: $" + amount);

        // Set up the input
        final EditText cardNumberInput = new EditText(activity);
        cardNumberInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        cardNumberInput.setHint("Card Number");

        final EditText expiryInput = new EditText(activity);
        expiryInput.setInputType(InputType.TYPE_CLASS_TEXT);
        expiryInput.setHint("MM/YY");

        final EditText cvvInput = new EditText(activity);
        cvvInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        cvvInput.setHint("CVV");

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(cardNumberInput);
        layout.addView(expiryInput);
        layout.addView(cvvInput);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Pay", (dialog, which) -> {
            String cardNumber = cardNumberInput.getText().toString();
            String expiry = expiryInput.getText().toString();
            String cvv = cvvInput.getText().toString();

            if (validateCardDetails(cardNumber, expiry, cvv)) {
                listener.onPaymentSuccess();
            } else {
                listener.onPaymentError("Invalid card details");
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            listener.onPaymentCanceled();
        });

        builder.show();
    }

    private boolean validateCardDetails(String cardNumber, String expiry, String cvv) {
        // expiry require a /
        return cardNumber.length() >= 16 &&
                expiry.matches("\\d{2}/\\d{2}") &&
                cvv.length() >= 3;
    }
}
