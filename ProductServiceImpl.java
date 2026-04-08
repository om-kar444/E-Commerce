package E_CommerceWebsite;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private List<Product> products = new ArrayList<Product>();

    public ProductServiceImpl() {
        products.add(new Product(1, "Laptop", 50000));
        products.add(new Product(2, "Smartphone", 20000));
        products.add(new Product(3, "Headphones", 1500));
        products.add(new Product(4, "Keyboard", 1200));
    }

    public void displayProducts() {
        System.out.println("----- Product Catalog -----");
        for(Product p : products) p.display();
    }

    public Product getProductById(int id) {
        for(Product p : products) {
            if(p.getId() == id) return p;
        }
        return null;
    }
}