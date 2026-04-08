package E_CommerceWebsite;

public interface CartService {
    void addToCart(Product product, int quantity);
    void removeFromCart(int productId);
    void viewCart();
    double calculateTotal();
    boolean checkout(double amountEntered);
    boolean isCartEmpty();
}