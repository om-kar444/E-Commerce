# 🛒 ShopEasy — Java E-Commerce Console App

A beginner-friendly Java console application that simulates an online shopping experience.
Built as a learning project to demonstrate core Java concepts in a real-world context.

---

## 📌 What This App Does

- **Sign Up / Log In** as a user
- **Browse products** by category (Electronics, Food, Clothing, Sports)
- **Add / Remove items** from your cart
- **View your cart** with a formatted bill
- **Checkout** with payment validation

---

## 🗂️ Project Structure

```
E_CommerceWebsite2/
│
├── ECommerceApp.java          ← Main entry point (runs the app)
│
├── Models
│   ├── User.java              ← Stores user info (name, email, etc.)
│   ├── Product.java           ← Stores product info (id, name, price)
│   ├── CartItem.java          ← Holds a product + quantity in cart
│   └── Category.java          ← Enum: ELECTRONICS, FOOD, CLOTHING, SPORTS
│
├── Services (Interfaces)
│   ├── UserService.java       ← What user operations exist
│   ├── ProductService.java    ← What product operations exist
│   └── CartService.java       ← What cart operations exist
│
├── Implementations
│   ├── UserServiceImpl.java   ← Signup, login, validation logic
│   ├── ProductServiceImpl.java← Product list, search, filter
│   └── CartServiceImpl.java   ← Add, remove, checkout logic
│
└── Custom Exceptions
    ├── InvalidUserException.java    ← Bad signup/login input
    ├── InvalidProductException.java ← Product not found
    └── PaymentException.java        ← Invalid payment amount
```

---

## 🔁 App Flow

```
Start App
   │
   ├── 1. Sign Up ──► Validate inputs ──► Save user
   │
   ├── 2. Log In  ──► Check credentials ──► Enter Shop Menu
   │                                              │
   │                          ┌──────────────────┤
   │                          │                  │
   │                    Browse Products      View Cart
   │                          │                  │
   │                    Add to Cart ──────► Remove Item
   │                                            │
   │                                       Checkout ──► Payment ──► Order Confirmed ✔
   │
   └── 3. Exit
```

---

## ☕ Java Concepts Used

### 1. OOP — Object-Oriented Programming

| Concept | Where Used |
|---|---|
| **Encapsulation** | `User`, `Product`, `CartItem` — private fields + public getters |
| **Abstraction** | `UserService`, `ProductService`, `CartService` interfaces hide implementation details |
| **Inheritance** | `UserServiceImpl implements UserService`, custom exceptions extend `Exception` |
| **Composition** | `CartItem` HAS-A `Product` inside it |

**Example — Encapsulation in `Product.java`:**
```java
// private fields = no one can change them directly from outside
private int id;
private String name;
private double price;

// public getter = safe read-only access
public String getName() { return name; }
```

---

### 2. Interfaces

Interfaces act as a **contract** — they say *what* must be done, not *how*.

**Example — `CartService.java`:**
```java
public interface CartService {
    void addToCart(Product product, int quantity);
    void removeFromCart(int productId);
    boolean checkout(double amountEntered) throws PaymentException;
}
```
`CartServiceImpl.java` then provides the actual logic for each method.

---

### 3. Collections (ArrayList & List)

Used to store users, products, and cart items in memory.

**Example — `CartServiceImpl.java`:**
```java
private List<CartItem> cart = new ArrayList<>();

// Add item
cart.add(new CartItem(product, quantity));

// Loop through items
for (CartItem item : cart) {
    System.out.println(item.getProduct().getName());
}

// Clear cart after payment
cart.clear();
```

**Example — `ProductServiceImpl.java` (Stream filter by category):**
```java
List<Product> filtered = products.stream()
    .filter(p -> p.getCategory() == category)
    .collect(Collectors.toList());
```

---

### 4. Exception Handling

Custom exceptions are thrown when something goes wrong, and caught to show friendly messages.

**Three custom exceptions:**

| Exception | When thrown |
|---|---|
| `InvalidUserException` | Empty username, short password, bad email, bad mobile |
| `InvalidProductException` | Product ID not found in catalog |
| `PaymentException` | Payment amount is zero or negative |

**Example — throwing in `UserServiceImpl.java`:**
```java
if (username.length() < 3) {
    throw new InvalidUserException("Username must be at least 3 characters.");
}
```

**Example — catching in `ECommerceApp.java`:**
```java
try {
    userService.signup(username, password, email, mobile);
} catch (InvalidUserException e) {
    System.out.println("Signup failed: " + e.getMessage());
}
```

---

### 5. Enum

`Category.java` is an **enum** — a fixed set of named constants.

```java
public enum Category {
    ELECTRONICS, FOOD, CLOTHING, SPORTS;
}
```

Used to tag every product with a category and filter the product list.

---

### 6. Wrapper Classes

Java primitives (`int`, `double`) sometimes need to be treated as objects.

**Examples in the project:**
```java
// String.format wraps double for display
System.out.printf("Total: $%.2f", calculateTotal());

// matches() wraps String to validate mobile number
if (!mobile.matches("\\d{10}")) { ... }

// isEmpty() on String checks for blank input
if (username.trim().isEmpty()) { ... }
```

---

## 🔍 Key Functions Explained

### `UserServiceImpl.java`

| Method | What it does |
|---|---|
| `signup()` | Validates all fields, checks for duplicate username/email, saves new user |
| `login()` | Searches user list for matching username + password |
| `validateEmail()` | Checks for `@` symbol and valid domain like `.com` |
| `validateMobile()` | Ensures exactly 10 digits using regex `\d{10}` |

### `ProductServiceImpl.java`

| Method | What it does |
|---|---|
| `displayAllProducts()` | Loops through all 4 categories and prints each |
| `displayByCategory()` | Filters products by category using streams |
| `getProductById()` | Searches list by ID, throws `InvalidProductException` if not found |
| `getAvailableCategories()` | Returns all Category enum values as a List |

### `CartServiceImpl.java`

| Method | What it does |
|---|---|
| `addToCart()` | Adds new item or increases quantity if already in cart |
| `removeFromCart()` | Finds item by product ID and removes it from list |
| `viewCart()` | Prints a formatted table of all cart items + total |
| `calculateTotal()` | Loops through cart and sums up all `getTotalPrice()` values |
| `checkout()` | Validates payment amount, confirms order, clears cart |
| `isCartEmpty()` | Returns `true` if the cart list is empty |

### `ECommerceApp.java`

| Method | What it does |
|---|---|
| `main()` | Entry point — shows main menu in a loop |
| `handleSignup()` | Reads user inputs and calls `userService.signup()` |
| `handleLogin()` | Reads credentials and calls `userService.login()` |
| `handleBrowseByCategory()` | Shows category menu, then products in chosen category |
| `handleAddToCart()` | Browse → pick product ID + quantity → add to cart |
| `handlePayment()` | Shows bill, reads amount, calls `cartService.checkout()` |
| `readInt()` / `readDouble()` | Safe input readers — catch `InputMismatchException` |

---

## 🚀 How to Run

1. **Clone or download** this project
2. Open in **IntelliJ IDEA** or any Java IDE
3. Make sure the package is `E_CommerceWebsite2`
4. Run `ECommerceApp.java`
5. Follow the on-screen menu!

> ☕ Requires **Java 8** or above.

---

## 👨‍💻 Made with

- Pure Java (no frameworks, no databases)
- Console I/O with `Scanner`
- In-memory storage using `ArrayList`

---


## ⚡ Why This Project Stands Out
 
> **A fully functional e-commerce simulation built with pure Java — no frameworks, no libraries, just core Java concepts in action.**
 
Most beginners use tutorials that show one concept at a time. This project **combines everything together** in one real working app:
 
**No Spring. No Maven. No database. Just Java.**
 
```java
// Real validation — not just a print statement
private void validateEmail(String email) throws InvalidUserException {
    int atIndex = email.indexOf('@');
    if (atIndex <= 0)
        throw new InvalidUserException("Email must contain '@' (e.g., user@gmail.com).");
    String domain = email.substring(atIndex + 1);
    if (!domain.contains("."))
        throw new InvalidUserException("Email domain must contain '.' (e.g., gmail.com).");
}
```
 
```java
// Real cart logic — updates quantity if item already exists
public void addToCart(Product product, int quantity) {
    for (CartItem item : cart) {
        if (item.getProduct().getId() == product.getId()) {
            item.setQuantity(item.getQuantity() + quantity); // update existing
            return;
        }
    }
    cart.add(new CartItem(product, quantity)); // else add new
}
```
 
```java
// Real payment flow — throws custom exception for bad input
public boolean checkout(double amountEntered) throws PaymentException {
    if (amountEntered <= 0)
        throw new PaymentException("Payment amount must be positive. Entered: $" + amountEntered);
    if (amountEntered == calculateTotal()) {
        cart.clear(); // order confirmed, cart reset
        return true;
    }
    return false;
}
```
 
**What makes it complete:**
- ✔ 100% Pure Java — no external dependencies
- ✔ Real input validation with custom exceptions
- ✔ Interface + Implementation pattern (like real projects)
- ✔ 20 products across 4 categories, fully browsable
- ✔ Cart persists across menu actions during session

- 
*Built as a Java learning project covering OOP, Collections, Exception Handling, Interfaces, Enums, and Wrapper Classes.*


