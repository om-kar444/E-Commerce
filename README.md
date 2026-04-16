# 🛒 E-Commerce Console Application (Java)

A Java-based console e-commerce application demonstrating core OOP concepts including **Encapsulation**, **Inheritance**, **Interfaces**, **Exception Handling**, **Collections**, and **Wrapper Classes**.

---

## 📁 Project Structure

```
E_CommerceWebsite2/
├── ECommerceApp.java          # Main entry point
├── User.java                  # User model
├── UserService.java           # User interface
├── UserServiceImpl.java       # User logic (signup/login/validation)
├── Product.java               # Product model
├── ProductService.java        # Product interface
├── ProductServiceImpl.java    # Product catalog & search
├── Category.java              # Enum for product categories
├── CartItem.java              # Cart item (product + quantity)
├── CartService.java           # Cart interface
├── CartServiceImpl.java       # Cart logic (add/remove/checkout)
├── InvalidUserException.java  # Custom checked exception
├── InvalidProductException.java # Custom unchecked exception
└── PaymentException.java      # Custom checked exception
```

---

## 📄 File-by-File Explanation

---

### 1. `User.java` — User Model (Encapsulation)

**Concept Used: Encapsulation (OOP)**

All fields are `private` and only accessible via `public` getters. No setters — data is set once via constructor (immutable pattern).

```java
public class User {
    private String username;   // private = Encapsulation
    private String password;
    private String email;
    private String mobileNumber;

    public User(String username, String password, String email, String mobileNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public String getUsername()     { return username; }   // public getter
    public String getPassword()     { return password; }
    public String getEmail()        { return email; }
    public String getMobileNumber() { return mobileNumber; }
}
```

> 🔑 **How it works:** Fields are hidden (`private`). Outside classes can only *read* them via getters — they cannot modify the data directly. This is the core of **Encapsulation**.

---

### 2. `UserService.java` — User Interface

**Concept Used: Interface (OOP)**

Defines a *contract* — what methods any user service must provide, without giving any implementation.

```java
public interface UserService {
    boolean signup(String username, String password, String email, String mobile)
            throws InvalidUserException;   // checked exception declared here

    boolean login(String username, String password);
}
```

> 🔑 **How it works:** An interface is a 100% abstract blueprint. Any class that `implements` this interface *must* provide bodies for `signup()` and `login()`. This enforces a consistent API.

---

### 3. `UserServiceImpl.java` — Signup, Login & Validation

**Concepts Used: Interface Implementation, Collections (ArrayList), Exception Handling**

```java
public class UserServiceImpl implements UserService {

    private List<User> users = new ArrayList<>();   // Collection: stores all registered users

    @Override
    public boolean signup(String username, String password, String email, String mobile)
            throws InvalidUserException {

        validateUsername(username);    // throws InvalidUserException if invalid
        validatePassword(password);
        validateEmail(email);
        validateMobile(mobile);

        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                throw new InvalidUserException("Username '" + username + "' is already taken!");
            }
        }

        users.add(new User(username, password, email, mobile));  // adds to ArrayList
        return true;
    }

    private void validateEmail(String email) throws InvalidUserException {
        int atIndex = email.indexOf('@');
        if (atIndex <= 0)
            throw new InvalidUserException("Email must contain '@'.");
        // ... more checks
    }

    private void validateMobile(String mobile) throws InvalidUserException {
        if (!mobile.matches("\\d{10}"))   // Regex: exactly 10 digits
            throw new InvalidUserException("Mobile must be 10 digits.");
    }
}
```

> 🔑 **How it works:**
> - `implements UserService` → fulfills the interface contract
> - `ArrayList<User>` → stores registered users in memory (Collection)
> - `validateEmail()`, `validateMobile()` → throw `InvalidUserException` if input is bad (Exception Handling)
> - Duplicate username/email check → iterates the list before adding

---

### 4. `Category.java` — Product Categories (Enum)

**Concept Used: Enum (special OOP class for fixed constants)**

```java
public enum Category {
    ELECTRONICS,
    FOOD,
    CLOTHING,
    SPORTS;

    public String displayName() {
        switch (this) {
            case ELECTRONICS: return "Electronics";
            case FOOD:        return "Food";
            case CLOTHING:    return "Clothing";
            case SPORTS:      return "Sports";
            default:          return this.name();
        }
    }
}
```

> 🔑 **How it works:** An `enum` is a fixed set of named constants. `Category.ELECTRONICS` is type-safe — you can't accidentally pass an invalid string as a category. `displayName()` adds human-friendly output.

---

### 5. `Product.java` — Product Model

**Concepts Used: Encapsulation, Enum as a type**

```java
public class Product {
    private int id;
    private String name;
    private double price;
    private Category category;   // Enum used as a field type

    public Product(int id, String name, double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void display() {
        System.out.printf("  [%2d] %-20s $%8.2f   [%s]%n",
                id, name, price, category.displayName());  // calls enum method
    }
}
```

> 🔑 **How it works:** Same encapsulation pattern as `User`. `Category` enum is used as the type for the `category` field — this prevents invalid values. `display()` formats output using `printf`.

---

### 6. `ProductService.java` — Product Interface

**Concept Used: Interface, Collections (List return type)**

```java
public interface ProductService {
    void displayAllProducts();
    void displayByCategory(Category category);
    Product getProductById(int id) throws InvalidProductException;  // Exception declared
    List<Category> getAvailableCategories();   // Returns a Collection
}
```

> 🔑 **How it works:** This interface forces any product service to support browsing, filtering, and searching. The `throws` keyword declares that `getProductById` can fail — callers must handle it.

---

### 7. `ProductServiceImpl.java` — Product Catalog & Search

**Concepts Used: Collections (ArrayList + Streams), Exception Handling**

```java
public class ProductServiceImpl implements ProductService {

    private List<Product> products = new ArrayList<>();  // Collection: product catalog

    public ProductServiceImpl() {
        products.add(new Product(101, "Laptop", 50000.00, Category.ELECTRONICS));
        products.add(new Product(201, "Basmati Rice 5kg", 450.00, Category.FOOD));
        // ... 20 products total across 4 categories
    }

    @Override
    public void displayByCategory(Category category) {
        List<Product> filtered = products.stream()          // Stream API
                .filter(p -> p.getCategory() == category)  // Lambda filter
                .collect(Collectors.toList());              // collect to new List
        for (Product p : filtered) p.display();
    }

    @Override
    public Product getProductById(int id) throws InvalidProductException {
        for (Product p : products) {
            if (p.getId() == id) return p;
        }
        throw new InvalidProductException("No product found with ID: " + id);  // Custom exception
    }
}
```

> 🔑 **How it works:**
> - `ArrayList<Product>` holds 20 products seeded in the constructor
> - `.stream().filter().collect()` filters by category using Java Streams (functional-style)
> - If `getProductById()` finds no match, it throws `InvalidProductException` instead of returning `null`

---

### 8. `CartItem.java` — Cart Item Model

**Concept Used: Encapsulation, Object Composition**

```java
public class CartItem {
    private Product product;   // Object composition: CartItem HAS-A Product
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;   // delegates to Product
    }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
```

> 🔑 **How it works:** `CartItem` *wraps* a `Product` with a quantity. `getTotalPrice()` uses the product's price to compute the line total. This is **Object Composition** — CartItem has-a Product.

---

### 9. `CartService.java` — Cart Interface

**Concept Used: Interface, Exception Handling (throws declaration)**

```java
public interface CartService {
    void addToCart(Product product, int quantity);
    void removeFromCart(int productId);
    void viewCart();
    double calculateTotal();
    boolean checkout(double amountEntered) throws PaymentException;  // declares exception
    boolean isCartEmpty();
}
```

> 🔑 **How it works:** The `checkout()` method declares `throws PaymentException` — any class implementing this interface must either handle or propagate this checked exception.

---

### 10. `CartServiceImpl.java` — Cart Logic

**Concepts Used: Collections (ArrayList), Exception Handling, Wrapper Classes**

```java
public class CartServiceImpl implements CartService {

    private List<CartItem> cart = new ArrayList<>();   // Collection: cart items

    @Override
    public void addToCart(Product product, int quantity) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);  // update if exists
                return;
            }
        }
        cart.add(new CartItem(product, quantity));   // add new item
    }

    @Override
    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cart) total += item.getTotalPrice();
        return total;
    }

    @Override
    public boolean checkout(double amountEntered) throws PaymentException {
        if (amountEntered <= 0) {
            throw new PaymentException("Amount must be positive. Entered: $" + amountEntered);
        }
        double total = calculateTotal();
        if (amountEntered == total) {
            cart.clear();    // Collection: empties the cart on success
            return true;
        }
        return false;
    }
}
```

> 🔑 **How it works:**
> - `ArrayList<CartItem>` stores current cart items
> - `addToCart()` first checks if product already exists → updates quantity; else adds new `CartItem`
> - `checkout()` throws `PaymentException` for invalid amounts — caller must catch it
> - `cart.clear()` empties the `ArrayList` after successful payment

---

### 11. `InvalidUserException.java` — Checked Exception

**Concept Used: Custom Exception, Inheritance (`extends Exception`)**

```java
public class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);   // calls Exception's constructor
    }
}
```

> 🔑 **How it works:** Extends `Exception` (not `RuntimeException`) → this is a **checked exception**. The compiler forces callers of `signup()` to either `try-catch` it or declare `throws InvalidUserException`.

---

### 12. `InvalidProductException.java` — Unchecked Exception

**Concept Used: Custom Exception, Inheritance (`extends RuntimeException`)**

```java
public class InvalidProductException extends RuntimeException {
    public InvalidProductException(String message) {
        super(message);
    }
}
```

> 🔑 **How it works:** Extends `RuntimeException` → this is an **unchecked exception**. Callers are NOT forced by the compiler to handle it — but they can if they want. Used for "programmer errors" like invalid product IDs.

---

### 13. `PaymentException.java` — Checked Exception

**Concept Used: Custom Exception, Inheritance (`extends Exception`)**

```java
public class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}
```

> 🔑 **How it works:** Same pattern as `InvalidUserException` — a checked exception for payment failures. The `checkout()` method throws this when the entered amount is invalid.

---

## 🧠 Concepts Summary Table

| Concept | Where Used | How |
|---|---|---|
| **Encapsulation** | `User`, `Product`, `CartItem` | `private` fields + `public` getters |
| **Interface** | `UserService`, `ProductService`, `CartService` | Contract-based design |
| **Interface Implementation** | `*ServiceImpl` classes | `implements` keyword, `@Override` |
| **Enum** | `Category` | Fixed set of category constants |
| **Object Composition** | `CartItem` | `CartItem` has-a `Product` |
| **Collections (ArrayList)** | `UserServiceImpl`, `ProductServiceImpl`, `CartServiceImpl` | Stores users, products, cart items |
| **Stream API** | `ProductServiceImpl` | `.stream().filter().collect()` for category filtering |
| **Custom Exceptions** | `InvalidUserException`, `InvalidProductException`, `PaymentException` | Meaningful error messages |
| **Checked Exception** | `InvalidUserException`, `PaymentException` | Extends `Exception` — compiler enforced |
| **Unchecked Exception** | `InvalidProductException` | Extends `RuntimeException` — optional handling |
| **Exception Handling** | `UserServiceImpl`, `CartServiceImpl` | `throw`, `throws`, `try-catch` |
| **Wrapper Classes** | Throughout | `String`, `Double`, `Integer` in Collections and formatting |

---

## ▶️ How to Run

```bash
# Compile all files
javac E_CommerceWebsite2/*.java

# Run the main application
java E_CommerceWebsite2.ECommerceApp
```

---

## 🔄 Application Flow

```
Start
  └── Signup (validates username, password, email, mobile)
        └── Login (checks credentials)
              └── Browse Products (by category or all)
                    └── Add to Cart (by product ID + quantity)
                          └── View Cart (formatted table with totals)
                                └── Checkout (enter exact amount → PaymentException if invalid)
                                      └── Order Confirmed / Error Message
```
