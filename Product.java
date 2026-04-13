package E_CommerceWebsite2;

// OOP: Product is a model class encapsulating product data
// Access Modifier: public class, private fields (Encapsulation)
public class Product {

    // Access Modifier: private — only accessible via getters
    private int id;
    private String name;
    private double price;
    private Category category; // OOP: using Category enum as a type

    // Constructor: initializes all fields including category
    public Product(int id, String name, double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Access Modifier: public getters — expose read-only access
    public int getId()           { return id; }
    public String getName()      { return name; }
    public double getPrice()     { return price; }
    public Category getCategory(){ return category; }

    // Displays product info in a formatted row
    public void display() {
        // Wrapper class: Double.valueOf used implicitly; String.format wraps primitives
        System.out.printf("  [%2d] %-20s $%8.2f   [%s]%n",
                id, name, price, category.displayName());
    }
}