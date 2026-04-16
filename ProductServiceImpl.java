package E_CommerceWebsite2;

import java.util.ArrayList;   // Collections: ArrayList implementation
import java.util.Arrays;      // Collections: Arrays.asList utility
import java.util.List;        // Collections: List interface
import java.util.stream.Collectors; // Collections: stream filtering

// OOP: Implements ProductService interface
// Inheritance: implements ProductService
// Access Modifier: public class
public class ProductServiceImpl implements ProductService {

    // Collections: ArrayList<Product> — stores product catalog
    // Access Modifier: private — internal catalog, not directly accessible
    private List<Product> products = new ArrayList<>();

    // Constructor: seeds the product catalog with items across all 4 categories
    public ProductServiceImpl() {

        // ── Category 1: ELECTRONICS ──
        products.add(new Product(101, "Laptop", 50000.00, Category.ELECTRONICS));
        products.add(new Product(102, "Smartphone", 20000.00, Category.ELECTRONICS));
        products.add(new Product(103, "Headphones", 1500.00, Category.ELECTRONICS));
        products.add(new Product(104, "Mechanical Keyboard", 2200.00, Category.ELECTRONICS));
        products.add(new Product(105, "Smart Watch", 8500.00, Category.ELECTRONICS));

        // ── Category 2: FOOD ──
        products.add(new Product(201, "Basmati Rice 5kg", 450.00, Category.FOOD));
        products.add(new Product(202, "Olive Oil 1L", 650.00, Category.FOOD));
        products.add(new Product(203, "Dark Chocolate Box", 380.00, Category.FOOD));
        products.add(new Product(204, "Mixed Nuts 500g", 520.00, Category.FOOD));
        products.add(new Product(205, "Green Tea 100bags", 290.00, Category.FOOD));

        // ── Category 3: CLOTHING ──
        products.add(new Product(301, "Cotton T-Shirt", 599.00, Category.CLOTHING));
        products.add(new Product(302, "Denim Jeans", 1299.00, Category.CLOTHING));
        products.add(new Product(303, "Running Shoes", 2499.00, Category.CLOTHING));
        products.add(new Product(304, "Hooded Sweatshirt", 899.00, Category.CLOTHING));
        products.add(new Product(305, "Formal Shirt", 799.00, Category.CLOTHING));

        // ── Category 4: SPORTS ──
        products.add(new Product(401, "Cricket Bat", 1800.00, Category.SPORTS));
        products.add(new Product(402, "Football", 650.00, Category.SPORTS));
        products.add(new Product(403, "Yoga Mat", 499.00, Category.SPORTS));
        products.add(new Product(404, "Resistance Bands", 350.00, Category.SPORTS));
        products.add(new Product(405, "Badminton Racket", 1100.00, Category.SPORTS));
    }

    // Displays ALL products grouped by category
    @Override
    public void displayAllProducts() {
        // Collections: iterating over Category enum values
        for (Category cat : Category.values()) {
            displayByCategory(cat);
        }
    }

    // Displays products filtered by a specific category
    // Collections: uses stream + filter (functional-style collection operation)
    @Override
    public void displayByCategory(Category category) {
        // Collections: filter the ArrayList using stream
        List<Product> filtered = products.stream()
                .filter(p -> p.getCategory() == category)
                .collect(Collectors.toList());

        System.out.println("\n  ── " + category.displayName() + " ──────────────────────────────");
        System.out.printf("  %-6s %-22s %10s%n", "ID", "Product", "Price");
        System.out.println("  ────────────────────────────────────────────");
        for (Product p : filtered) p.display(); // OOP: polymorphic display call
        System.out.println("  ────────────────────────────────────────────");
    }

    // Returns a product by ID — throws custom exception if not found
    // Exception Handling: throws InvalidProductException (custom unchecked exception)
    @Override
    public Product getProductById(int id) throws InvalidProductException {
        // Collections: iterating ArrayList to search
        for (Product p : products) {
            if (p.getId() == id) return p;
        }
        // Exception Handling: throw informative exception instead of returning null
        throw new InvalidProductException("No product found with ID: " + id);
    }

    // Returns the list of available categories
    // Collections: Arrays.asList wraps array into a fixed List
    @Override
    public List<Category> getAvailableCategories() {
        return Arrays.asList(Category.values()); // Wrapper class: arrays → List
    }
}