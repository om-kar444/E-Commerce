package E_CommerceWebsite2;

// Exception Handling: thrown when payment validation fails
// Inheritance: extends Exception (checked)
public class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}
