package E_CommerceWebsite2;

import java.util.List; // Collections: List interface

// OOP: Interface — contract for product operations
// Access Modifier: public
public interface ProductService {
    void displayAllProducts();
    void displayByCategory(Category category);  // Browse by category
    Product getProductById(int id) throws InvalidProductException; // Exception Handling
    List<Category> getAvailableCategories();    // Collections: returns a List
}
