package E_CommerceWebsite2;

import java.util.ArrayList;  // Collections: ArrayList
import java.util.List;        // Collections: List interface

// OOP: Implements CartService interface (Interface implementation)
// Inheritance: implements CartService
// Access Modifier: public class
public class CartServiceImpl implements CartService {

    // Collections: ArrayList<CartItem> — holds items in the cart
    // Access Modifier: private — not accessible outside this class
    private List<CartItem> cart = new ArrayList<>();

    // Adds a product to cart; if already exists, updates quantity
    // OOP: method works with Product objects (object as parameter)
    @Override
    public void addToCart(Product product, int quantity) {
        // Collections: linear search through ArrayList
        for (CartItem item : cart) {
            // Wrapper class: Integer.valueOf comparison avoided by using == on primitives
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                System.out.printf("  ✔ Updated: %-20s → Qty: %d%n", product.getName(), item.getQuantity());
                return;
            }
        }
        // Collections: add new CartItem to list
        cart.add(new CartItem(product, quantity));
        System.out.printf("  ✔ Added: %-20s x%d to cart.%n", product.getName(), quantity);
    }

    // Removes a product from cart by product ID
    @Override
    public void removeFromCart(int productId) {
        // Collections: Iterator pattern — safe removal while iterating
        for (CartItem item : cart) {
            if (item.getProduct().getId() == productId) {
                System.out.println("  ✔ Removed: " + item.getProduct().getName() + " from cart.");
                cart.remove(item); // Collections: ArrayList.remove()
                return;
            }
        }
        // No explicit exception — just user-friendly message (cart removal is non-critical)
        System.out.println("  ✘ Product ID " + productId + " not found in cart.");
    }

    // Displays current cart contents in a formatted table
    @Override
    public void viewCart() {
        System.out.println("\n  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║              🛒  Your Cart                   ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");

        // Collections: isEmpty() on ArrayList
        if (cart.isEmpty()) {
            System.out.println("  Your cart is empty.\n");
            return;
        }

        System.out.printf("  %-6s %-22s %-6s %10s%n", "ID", "Product", "Qty", "Total");
        System.out.println("  ──────────────────────────────────────────────");

        // Collections: enhanced for-loop over ArrayList
        for (CartItem item : cart) {
            System.out.printf("  [%3d] %-22s x%-4d $%8.2f%n", item.getProduct().getId(), item.getProduct().getName(), item.getQuantity(), item.getTotalPrice());
        }

        System.out.println("  ──────────────────────────────────────────────");
        // Wrapper class: Double.toString implicitly used in String.format
        System.out.printf("  %-33s $%8.2f%n", "TOTAL:", calculateTotal());
        System.out.println("  ══════════════════════════════════════════════");
    }

    // Calculates grand total of all cart items
    // Wrapper class: double auto-boxed to Double in some Collection contexts
    @Override
    public double calculateTotal() {
        double total = 0;
        // Collections: iterating list to sum up totals
        for (CartItem item : cart) total += item.getTotalPrice();
        return total;
    }

    // Exception Handling: throws PaymentException for invalid payment input
    @Override
    public boolean checkout(double amountEntered) throws PaymentException {
        double total = calculateTotal();

        // Exception Handling: throw custom PaymentException for bad amount
        if (amountEntered <= 0) {
            throw new PaymentException("Payment amount must be positive. Entered: $" + amountEntered);
        }

        if (amountEntered == total) {
            System.out.println("\n  ✔ Payment of $" + String.format("%.2f", total) + " received.");
            System.out.println("  ✔ Order confirmed! Your items will arrive soon. 🎉");
            cart.clear(); // Collections: clear() empties the ArrayList
            return true;
        } else if (amountEntered < total) {
            System.out.printf("  ✘ Insufficient amount. Entered $%.2f but due is $%.2f.%n", amountEntered, total);
        } else {
            System.out.printf("  ✘ Overpayment. Please enter exactly $%.2f.%n", total);
        }
        return false;
    }

    // Returns true if cart has no items
    // Wrapper class: boolean primitive returned (auto-boxed to Boolean if needed)
    @Override
    public boolean isCartEmpty() {
        return cart.isEmpty(); // Collections: ArrayList.isEmpty()
    }
}