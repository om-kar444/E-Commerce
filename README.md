# 🛒 ShopEasy — Java E-Commerce Console App

A Java console application that simulates a full online shopping experience.  
Built to demonstrate core Java concepts: **OOP · Interfaces · Collections · Exception Handling · Enums · Wrapper Classes**

---

## 📌 Features

- Sign Up with full input validation (username, password, email, 10-digit mobile)
- Log In with credential matching
- Browse 20 products across 4 categories: Electronics, Food, Clothing, Sports
- Add / Remove items from cart (auto-updates quantity if item already in cart)
- View cart with formatted bill and running total
- Checkout with 3 payment methods: Cash on Delivery, UPI, Google Pay

---

## 🗂️ Project Structure

```
E_CommerceWebsite2/
│
├── ECommerceApp.java           ← Main entry point
│
├── Models
│   ├── User.java               ← Stores user data (encapsulation)
│   ├── Product.java            ← Stores product info (id, name, price, category)
│   ├── CartItem.java           ← Holds Product + quantity (Composition: HAS-A)
│   └── Category.java          ← Enum: ELECTRONICS, FOOD, CLOTHING, SPORTS
│
├── Services (Interfaces)
│   ├── UserService.java        ← Contract for signup/login
│   ├── ProductService.java     ← Contract for product operations
│   └── CartService.java        ← Contract for cart operations
│
├── Implementations
│   ├── UserServiceImpl.java    ← Signup validation, login logic, ArrayList<User>
│   ├── ProductServiceImpl.java ← Product catalog, stream filtering, search by ID
│   └── CartServiceImpl.java   ← Add/remove/checkout, ArrayList<CartItem>
│
└── Custom Exceptions
    ├── InvalidUserException.java    ← Bad signup/login input (checked)
    ├── InvalidProductException.java ← Product ID not found (unchecked)
    └── PaymentException.java        ← Invalid payment amount (checked)
```

---

## 🔁 App Flow

```
Start → Sign Up / Log In → Browse Products → Add to Cart → Checkout → Done ✔
```

---

## ☕ Java Concepts Used

### 1. OOP — Encapsulation
Private fields, public getters. No outside class can directly change a user's password.
```java
public class User {
    private String username;
    private String password;

    public String getUsername() { return username; }
}
```

### 2. OOP — Composition (HAS-A)
`CartItem` contains a `Product` object inside it.
```java
public class CartItem {
    private Product product;   // CartItem HAS-A Product
    private int quantity;

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
```

### 3. Interfaces
Define *what* must exist — not *how*. `CartServiceImpl` provides the how.
```java
public interface CartService {
    void addToCart(Product product, int quantity);
    boolean checkout(double amount) throws PaymentException;
}
```

### 4. Inheritance
Custom exceptions extend `Exception` or `RuntimeException`.
`UserServiceImpl` implements `UserService`.
```java
public class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);
    }
}
```

### 5. Collections — ArrayList
All users, products, and cart items are stored in ArrayLists in memory.
```java
private List<CartItem> cart = new ArrayList<>();

cart.add(new CartItem(product, quantity));  // add
cart.remove(item);                          // remove
cart.clear();                               // empty after checkout
```

Filter products by category using Streams:
```java
List<Product> filtered = products.stream()
    .filter(p -> p.getCategory() == category)
    .collect(Collectors.toList());
```

### 6. Exception Handling
Thrown when something goes wrong, caught in `ECommerceApp.java`.
```java
// Throwing (in UserServiceImpl)
if (username.length() < 3)
    throw new InvalidUserException("Username must be at least 3 characters.");

// Catching (in ECommerceApp)
try {
    userService.signup(username, password, email, mobile);
} catch (InvalidUserException e) {
    System.out.println("Signup failed: " + e.getMessage());
}
```

### 7. Enum
`Category` is an enum — exactly 4 fixed valid values, nothing else accepted.
```java
public enum Category {
    ELECTRONICS, FOOD, CLOTHING, SPORTS;
}

new Product(101, "Laptop", 50000.00, Category.ELECTRONICS);
```

### 8. Wrapper Classes
String utility methods used throughout for validation and formatting.
```java
if (!mobile.matches("\\d{10}"))   // regex: exactly 10 digits
    throw new InvalidUserException("Mobile must be 10 digits.");

System.out.printf("Total: $%.2f", calculateTotal());  // double formatting
```

---

## 🚀 How to Run

1. Clone or download this project
2. Open in **IntelliJ IDEA** or any Java IDE
3. Make sure package is `E_CommerceWebsite2`
4. Run `ECommerceApp.java`
5. Follow the on-screen menu

> ☕ Requires **Java 8** or above. No external libraries needed.

---

*Built with pure Java — no frameworks, no database. Everything stored in-memory using ArrayList.*
