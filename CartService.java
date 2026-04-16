package E_CommerceWebsite2;

// OOP: Interface — contract for cart operations
// Access Modifier: public
public interface CartService {
    void addToCart(Product product, int quantity);

    void removeFromCart(int productId);

    void viewCart();

    double calculateTotal();

    // Exception Handling: checkout throws PaymentException on invalid payment
    boolean checkout(double amountEntered) throws PaymentException;

    boolean isCartEmpty();
}
