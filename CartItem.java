package E_CommerceWebsite2;

// OOP: CartItem is a helper model class (Encapsulation)
// Access Modifier: public class
public class CartItem {

    // Access Modifier: private fields — Encapsulation
    private Product product;  // OOP: composition — CartItem HAS-A Product
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Access Modifier: public getters and setter
    public Product getProduct()          { return product; }
    public int getQuantity()             { return quantity; }
    public void setQuantity(int quantity){ this.quantity = quantity; }

    // Wrapper class: Double.parseDouble not used here, but primitive double is auto-boxed
    // when stored in Collections — demonstrates boxing behavior
    public double getTotalPrice() {
        return product.getPrice() * quantity; // auto-boxing when used in Double context
    }
}
