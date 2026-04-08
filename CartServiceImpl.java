package E_CommerceWebsite;

import java.util.ArrayList;
import java.util.List;

public class CartServiceImpl implements CartService {
    private List<CartItem> cart = new ArrayList<CartItem>();

    public void addToCart(Product product, int quantity) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                System.out.println("  Updated: " + product.getName() + " -> Qty: " + item.getQuantity());
                return;
            }
        }
        cart.add(new CartItem(product, quantity));
        System.out.println("  ✔ Added: " + product.getName() + " x" + quantity + " to cart.");
    }

    public void removeFromCart(int productId) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == productId) {
                System.out.println("  ✔ Removed: " + item.getProduct().getName() + " from cart.");
                cart.remove(item);
                return;
            }
        }
        System.out.println("  ✘ Product with ID " + productId + " not found in cart.");
    }

    // Shows what's in the cart — used before asking which item to remove
    public void viewCart() {
        System.out.println("\n  ╔══════════════════════════════╗");
        System.out.println("  ║         Your Cart            ║");
        System.out.println("  ╚══════════════════════════════╝");
        if (cart.isEmpty()) {
            System.out.println("  Cart is empty.");
            return;
        }
        System.out.println("  ─────────────────────────────────");
        for (CartItem item : cart) {
            System.out.printf("  [%d] %-15s x%-3d  $%.2f%n",
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice());
        }
        System.out.println("  ─────────────────────────────────");
        System.out.printf("  TOTAL:                       $%.2f%n", calculateTotal());
        System.out.println("  ─────────────────────────────────");
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cart) total += item.getTotalPrice();
        return total;
    }

    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    /**
     * Simulates a payment flow.
     * Returns true if payment succeeds (entered amount matches total).
     */
    public boolean checkout(double amountEntered) {
        double total = calculateTotal();
        if (amountEntered == total) {
            System.out.println("\n  ✔ Payment of $" + String.format("%.2f", total) + " received.");
            System.out.println("  ✔ Order confirmed! Your items are on the way. 🎉");
            cart.clear();
            return true;
        } else if (amountEntered < total) {
            System.out.printf("  ✘ Insufficient amount. You entered $%.2f but total is $%.2f.%n", amountEntered, total);
        } else {
            System.out.printf("  ✘ Incorrect amount. Please enter exactly $%.2f.%n", total);
        }
        return false;
    }
}