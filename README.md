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

#### 🧩 Concepts Used
- **Encapsulation** — all 4 fields are `private`, hidden from outside classes
- **Constructor** — all fields are set at object creation time, not after
- **Immutability pattern** — no setters exist, so once a `User` is created, its data cannot be changed
- **Access Modifiers** — `private` on fields, `public` on getters — controlled access
- **Wrapper Class** — `String` is a wrapper/reference type used for all text fields

#### 📌 Key Points
- `private` keyword ensures no other class can directly do `user.password = "hack"` — they must go through the getter
- No `setPassword()` or `setEmail()` exists — this is intentional to prevent accidental data mutation
- `this.username = username` inside the constructor uses `this` to distinguish the field from the parameter
- All 4 getters return the raw field value — read-only access from outside

#### 💻 Snippet 1 — Private Fields & Constructor
```java
public class User {
    // Encapsulation: all fields private — not directly accessible outside
    private String username;
    private String password;
    private String email;
    private String mobileNumber;

    // Constructor: sets all fields once at creation
    public User(String username, String password, String email, String mobileNumber) {
        this.username = username;       // 'this' refers to current object's field
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }
}
```

#### 💻 Snippet 2 — Public Getters (Read-Only Access)
```java
    // public getters — the ONLY way to read these fields from outside
    public String getUsername()     { return username; }
    public String getPassword()     { return password; }
    public String getEmail()        { return email; }
    public String getMobileNumber() { return mobileNumber; }

    // ❌ No setters — data cannot be changed after object is created
    // This prevents: user.setPassword("hacked") from being possible
```

> 🔑 **How it works:**
> - Fields are hidden (`private`) — direct access like `user.password` causes a compile error
> - Outside classes call `user.getPassword()` to read — but cannot write
> - This pattern (private fields + public getters) is the foundation of **Encapsulation** in OOP
> - `String` itself is a Wrapper class — it wraps character data into an object with built-in methods like `.equals()`, `.length()`, `.trim()`

---

### 2. `UserService.java` — User Interface

#### 🧩 Concepts Used
- **Interface** — a pure contract with no implementation, only method signatures
- **Abstraction** — hides *how* signup/login works; only exposes *what* they do
- **Exception Declaration** — `throws InvalidUserException` is declared at the interface level, forcing all implementations to handle it
- **Return Types** — `boolean` return tells the caller whether the operation succeeded

#### 📌 Key Points
- An interface contains zero implementation — no method body, no fields (except constants)
- Any class that says `implements UserService` *must* define both `signup()` and `login()` or it won't compile
- Declaring `throws InvalidUserException` on the interface forces every implementing class to either throw or handle it
- This makes it easy to swap `UserServiceImpl` for a different implementation (e.g., database-backed) without changing any calling code

#### 💻 Snippet 1 — Interface Definition
```java
public interface UserService {

    // Method signature only — no body, no logic
    // 'throws' forces callers to handle the checked exception
    boolean signup(String username, String password, String email, String mobile)
            throws InvalidUserException;

    boolean login(String username, String password);
    // No implementation here — just the contract
}
```

#### 💻 Snippet 2 — How the Interface is Used (from UserServiceImpl)
```java
// 'implements UserService' = this class fulfills the contract
public class UserServiceImpl implements UserService {

    @Override  // annotation confirms this method satisfies the interface
    public boolean signup(String username, String password, String email, String mobile)
            throws InvalidUserException {
        // actual logic lives here, not in the interface
    }

    @Override
    public boolean login(String username, String password) {
        // actual logic lives here
    }
}
```

> 🔑 **How it works:**
> - Interface = blueprint, `UserServiceImpl` = actual building
> - If `UserServiceImpl` forgets to implement `login()`, Java gives a compile-time error immediately
> - `@Override` annotation confirms the method correctly matches the interface signature
> - This pattern (interface + implementation) is called **Program to an Interface** — a key design principle

---

### 3. `UserServiceImpl.java` — Signup, Login & Validation

#### 🧩 Concepts Used
- **Interface Implementation** — `implements UserService` fulfills the contract
- **Collections** — `ArrayList<User>` stores all registered users in memory
- **Exception Handling** — throws `InvalidUserException` for every validation failure
- **Access Modifiers** — `private` helper validators are internal only, not exposed
- **Wrapper Classes** — `String` methods like `.equalsIgnoreCase()`, `.matches()`, `.indexOf()` used throughout

#### 📌 Key Points
- `ArrayList<User>` acts as an in-memory database — all users are lost when the program exits
- Each validation step (`validateUsername`, `validatePassword`, `validateEmail`, `validateMobile`) is a separate `private` method — keeps code clean and single-responsibility
- Duplicate check uses `.equalsIgnoreCase()` — so `"Alice"` and `"alice"` are treated as the same username
- Email validation checks: not empty → has `@` → has `.` after `@` → has valid TLD (e.g., `.com`)
- Mobile validation uses regex `\\d{10}` — exactly 10 numeric digits, nothing else
- `login()` does NOT throw an exception — it just returns `false` on failure (non-critical path)

#### 💻 Snippet 1 — ArrayList Collection + Signup Flow
```java
public class UserServiceImpl implements UserService {

    // Collection: ArrayList stores all users in memory (acts as in-memory DB)
    private List<User> users = new ArrayList<>();

    @Override
    public boolean signup(String username, String password, String email, String mobile)
            throws InvalidUserException {

        // Step 1: validate all inputs (throws if any field is invalid)
        validateUsername(username);
        validatePassword(password);
        validateEmail(email);
        validateMobile(mobile);

        // Step 2: check for duplicates in the ArrayList
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username))
                throw new InvalidUserException("Username '" + username + "' is already taken!");
            if (u.getEmail().equalsIgnoreCase(email))
                throw new InvalidUserException("Email '" + email + "' is already registered!");
        }

        // Step 3: all checks passed — add to the list
        users.add(new User(username, password, email, mobile));
        return true;
    }
}
```

#### 💻 Snippet 2 — Login Method
```java
    @Override
    public boolean login(String username, String password) {
        // Search through ArrayList for matching credentials
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                System.out.printf("  Welcome back, %s  |  📧 %s  |  📱 %s%n",
                        u.getUsername(), u.getEmail(), u.getMobileNumber());
                return true;   // found — login success
            }
        }
        System.out.println("  ✘ Invalid username or password.");
        return false;  // not found — login failed (no exception, just false)
    }
```

#### 💻 Snippet 3 — Private Validators (Exception Handling)
```java
    // Access Modifier: private — only used internally, not part of the interface
    private void validateUsername(String username) throws InvalidUserException {
        if (username == null || username.trim().isEmpty())
            throw new InvalidUserException("Username cannot be empty.");
        if (username.length() < 3)
            throw new InvalidUserException("Username must be at least 3 characters.");
    }

    private void validateEmail(String email) throws InvalidUserException {
        int atIndex = email.indexOf('@');         // Wrapper class: String.indexOf()
        if (atIndex <= 0)
            throw new InvalidUserException("Email must contain '@'.");

        String domain = email.substring(atIndex + 1);
        if (!domain.contains("."))
            throw new InvalidUserException("Email domain must contain '.'.");

        String tld = domain.substring(domain.lastIndexOf('.') + 1);
        if (tld.isEmpty())
            throw new InvalidUserException("Email must have a valid TLD like .com or .in");
    }

    private void validateMobile(String mobile) throws InvalidUserException {
        if (!mobile.matches("\\d{10}"))   // Regex: exactly 10 digits, no spaces/symbols
            throw new InvalidUserException("Mobile must be exactly 10 digits.");
    }
```

> 🔑 **How it works:**
> - `implements UserService` — this class provides the actual body for both interface methods
> - `ArrayList<User>` — a resizable list that grows as users register
> - Each `validate*()` method throws `InvalidUserException` immediately if a rule is broken — the signup stops at the first failure
> - `login()` returns `false` instead of throwing — login failure is expected behavior, not an error
> - `private` validators cannot be called from outside — they are internal helpers only

---

### 4. `Category.java` — Product Categories (Enum)

#### 🧩 Concepts Used
- **Enum** — a special Java class that holds a fixed set of named constants
- **Type Safety** — using `Category` as a type prevents invalid strings like `"Electronix"` from being passed
- **Method in Enum** — `displayName()` adds behaviour to a constant, making enums more powerful than plain strings
- **Switch Statement** — used inside `displayName()` to return a human-readable label per constant

#### 📌 Key Points
- Enums are compile-time constants — `Category.ELECTRONICS` will never be `null` or misspelled
- `Category.values()` returns an array of all 4 constants — used in `ProductServiceImpl` to loop over all categories
- Adding a method (`displayName()`) inside an enum makes it behave like a class, not just a list of labels
- If you add a new category (e.g., `BOOKS`), the compiler will warn you everywhere a `switch` on `Category` exists — safer than using raw strings

#### 💻 Snippet 1 — Enum Constants
```java
public enum Category {
    ELECTRONICS,   // constant 1
    FOOD,          // constant 2
    CLOTHING,      // constant 3
    SPORTS;        // constant 4 — semicolon needed when methods follow
}
```

#### 💻 Snippet 2 — Method Inside Enum
```java
    // Enum can have methods — returns a readable string for each constant
    public String displayName() {
        switch (this) {   // 'this' refers to the current enum constant
            case ELECTRONICS: return "Electronics";
            case FOOD:        return "Food";
            case CLOTHING:    return "Clothing";
            case SPORTS:      return "Sports";
            default:          return this.name();  // fallback: returns "ELECTRONICS" etc.
        }
    }
```

#### 💻 Snippet 3 — How Enum is Used in Other Classes
```java
// In Product.java — Category used as a field type
private Category category;
new Product(101, "Laptop", 50000.00, Category.ELECTRONICS);  // type-safe, can't pass "Electronics"

// In ProductServiceImpl.java — iterating all enum values
for (Category cat : Category.values()) {   // [ELECTRONICS, FOOD, CLOTHING, SPORTS]
    displayByCategory(cat);
}

// In ProductServiceImpl.java — filtering by category
.filter(p -> p.getCategory() == category)  // enum comparison with ==, not .equals()
```

> 🔑 **How it works:**
> - `Category.ELECTRONICS` is a singleton constant — you compare with `==` (not `.equals()`)
> - `Category.values()` gives you `[ELECTRONICS, FOOD, CLOTHING, SPORTS]` — great for looping
> - `displayName()` lets you print `"Electronics"` instead of the raw constant name `"ELECTRONICS"`
> - If you try to pass a `String` where a `Category` is expected, Java gives a compile error — this is **type safety**

---

### 5. `Product.java` — Product Model

#### 🧩 Concepts Used
- **Encapsulation** — all fields `private`, exposed only via `public` getters
- **Enum as a field type** — `Category` enum used instead of a raw `String` for type safety
- **Formatted Output** — `System.out.printf()` with format specifiers for aligned display
- **Object Design** — `display()` method keeps formatting logic inside the class (responsibility principle)

#### 📌 Key Points
- `id`, `name`, `price`, `category` are all `private` — no direct access from outside
- `Category category` as a field means only valid enum values are accepted — you cannot accidentally assign `"Electrnics"` (typo)
- `display()` uses `%2d`, `%-20s`, `%8.2f` format specifiers — right-aligns ID, left-aligns name, formats price to 2 decimal places
- `category.displayName()` delegates to the enum method — Product doesn't need to know how to format the category name

#### 💻 Snippet 1 — Private Fields + Constructor
```java
public class Product {
    private int id;               // primitive — product ID
    private String name;          // Wrapper class — product name
    private double price;         // primitive — product price
    private Category category;    // Enum type — only valid categories allowed

    // Constructor: all 4 fields required at creation
    public Product(int id, String name, double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
```

#### 💻 Snippet 2 — Getters
```java
    // public getters — read-only access to private fields
    public int getId()           { return id; }
    public String getName()      { return name; }
    public double getPrice()     { return price; }
    public Category getCategory(){ return category; }

    // Note: no setters — product data doesn't change after creation
```

#### 💻 Snippet 3 — Display Method with printf Formatting
```java
    // Formats and prints product in a clean table row
    public void display() {
        System.out.printf("  [%2d] %-20s $%8.2f   [%s]%n",
                id,                      // %2d  → right-aligned integer, width 2
                name,                    // %-20s → left-aligned string, width 20
                price,                   // %8.2f → right-aligned float, 2 decimal places
                category.displayName()); // calls enum method → "Electronics", "Food" etc.
    }
    // Output example:
    // [101] Laptop               $ 50000.00   [Electronics]
    // [201] Basmati Rice 5kg     $   450.00   [Food]
```

#### 💻 Snippet 4 — How Products Are Created (from ProductServiceImpl)
```java
// Category enum ensures only valid categories can be passed
products.add(new Product(101, "Laptop",           50000.00, Category.ELECTRONICS));
products.add(new Product(201, "Basmati Rice 5kg",   450.00, Category.FOOD));
products.add(new Product(301, "Cotton T-Shirt",     599.00, Category.CLOTHING));
products.add(new Product(401, "Cricket Bat",       1800.00, Category.SPORTS));
// ❌ new Product(101, "Laptop", 50000.00, "Electronics") — compile error! String ≠ Category
```

> 🔑 **How it works:**
> - `Category category` as a field type means Java won't compile if you try to pass a plain string
> - Getters expose values, but nothing can modify a product's price or name after it's created
> - `display()` is a behaviour that belongs to `Product` — it knows its own fields, so it formats itself
> - `printf` format strings keep the output table aligned regardless of name/price length

---

### 6. `ProductService.java` — Product Interface

#### 🧩 Concepts Used
- **Interface** — defines what a product service must be able to do, not how
- **Abstraction** — callers don't need to know if products come from a file, DB, or hardcoded list
- **Collections in Interface** — `List<Category>` as a return type shows interfaces can use generics
- **Exception Declaration** — `throws InvalidProductException` baked into the contract

#### 📌 Key Points
- `displayAllProducts()` — shows every product across all categories
- `displayByCategory(Category)` — filters to one category; uses `Category` enum for type safety
- `getProductById(int id)` — search by ID; declared to throw `InvalidProductException` if not found
- `getAvailableCategories()` — returns a `List<Category>` (a collection), useful for building category menus
- Any future implementation (e.g., `DatabaseProductService`) must provide all 4 methods

#### 💻 Snippet 1 — Interface Contract
```java
public interface ProductService {

    void displayAllProducts();  // show all 20 products grouped by category

    void displayByCategory(Category category);  // filter by enum — type-safe

    // declares exception: caller must handle or rethrow
    Product getProductById(int id) throws InvalidProductException;

    List<Category> getAvailableCategories();  // returns Collection of category enums
}
```

> 🔑 **How it works:**
> - `ProductServiceImpl` implements this — but any other class could too (e.g., `DatabaseProductServiceImpl`)
> - The interface means calling code only depends on `ProductService`, not the concrete class — easy to swap implementations
> - `throws InvalidProductException` on the interface forces the implementing class to propagate or handle this exception

---

### 7. `ProductServiceImpl.java` — Product Catalog & Search

#### 🧩 Concepts Used
- **Collections** — `ArrayList<Product>` stores the 20-product catalog
- **Stream API** — `.stream().filter().collect()` for functional-style category filtering
- **Lambda Expression** — `p -> p.getCategory() == category` is an inline function
- **Exception Handling** — throws `InvalidProductException` when product ID not found
- **Constructor seeding** — product catalog is populated when the object is created

#### 📌 Key Points
- 20 products are added in the constructor — 5 per category (Electronics, Food, Clothing, Sports)
- `displayByCategory()` uses Java Streams to filter without writing a manual loop
- `getProductById()` does a linear search — if no match, throws exception instead of returning `null`
- `Arrays.asList(Category.values())` wraps the enum array into a `List` — this is a fixed-size list
- `displayAllProducts()` loops over `Category.values()` and calls `displayByCategory()` for each

#### 💻 Snippet 1 — ArrayList Catalog + Constructor Seeding
```java
public class ProductServiceImpl implements ProductService {

    // Collection: stores entire product catalog
    private List<Product> products = new ArrayList<>();

    public ProductServiceImpl() {
        // Seeded with 5 products per category at construction time
        products.add(new Product(101, "Laptop",              50000.00, Category.ELECTRONICS));
        products.add(new Product(102, "Smartphone",          20000.00, Category.ELECTRONICS));
        products.add(new Product(103, "Headphones",           1500.00, Category.ELECTRONICS));

        products.add(new Product(201, "Basmati Rice 5kg",      450.00, Category.FOOD));
        products.add(new Product(202, "Olive Oil 1L",           650.00, Category.FOOD));

        products.add(new Product(301, "Cotton T-Shirt",         599.00, Category.CLOTHING));
        products.add(new Product(302, "Denim Jeans",           1299.00, Category.CLOTHING));

        products.add(new Product(401, "Cricket Bat",           1800.00, Category.SPORTS));
        products.add(new Product(402, "Football",               650.00, Category.SPORTS));
        // ... 20 total
    }
}
```

#### 💻 Snippet 2 — Stream API + Lambda Filtering
```java
    @Override
    public void displayByCategory(Category category) {
        // Stream API: functional-style filtering of the ArrayList
        List<Product> filtered = products.stream()               // convert list to stream
                .filter(p -> p.getCategory() == category)        // lambda: keep matching items
                .collect(Collectors.toList());                   // collect results to new List

        // Without streams, this would be a manual for-loop with an if-check inside
        for (Product p : filtered) p.display();  // OOP: each product displays itself
    }
```

#### 💻 Snippet 3 — Search by ID + Custom Exception
```java
    @Override
    public Product getProductById(int id) throws InvalidProductException {
        // Linear search through ArrayList
        for (Product p : products) {
            if (p.getId() == id) return p;   // found — return immediately
        }
        // Not found — throw custom exception with helpful message
        // ✅ Better than returning null (which would cause NullPointerException later)
        throw new InvalidProductException("No product found with ID: " + id);
    }
```

#### 💻 Snippet 4 — Returning List of Categories
```java
    @Override
    public List<Category> getAvailableCategories() {
        // Arrays.asList wraps enum array into a List
        return Arrays.asList(Category.values());
        // Returns: [ELECTRONICS, FOOD, CLOTHING, SPORTS]
    }
```

> 🔑 **How it works:**
> - Constructor runs once when `new ProductServiceImpl()` is called — catalog is ready immediately
> - `.stream().filter(lambda).collect()` replaces a manual loop + if-check in one readable line
> - Throwing `InvalidProductException` instead of returning `null` prevents hidden `NullPointerException` bugs later
> - `Arrays.asList()` creates an unmodifiable list — you can read it but not add/remove from it

---

### 8. `CartItem.java` — Cart Item Model

#### 🧩 Concepts Used
- **Object Composition** — `CartItem` HAS-A `Product` (not inherits from it)
- **Encapsulation** — `private` fields, one setter (`setQuantity`) for controlled mutation
- **Delegation** — `getTotalPrice()` delegates price lookup to the `Product` object
- **Single Responsibility** — this class only knows about quantity + its product

#### 📌 Key Points
- `CartItem` wraps a `Product` reference — it does not copy the product's data, just holds a pointer to it
- `setQuantity()` is the only setter — because quantity can change (add more of same item), but the product itself doesn't change
- `getTotalPrice()` = `product.getPrice() * quantity` — delegates price to `Product`, not hardcoded
- When `CartServiceImpl` finds the same product already in cart, it calls `item.setQuantity(old + new)`

#### 💻 Snippet 1 — Object Composition (HAS-A Relationship)
```java
public class CartItem {

    // Composition: CartItem HAS-A Product (not IS-A Product)
    private Product product;   // holds reference to a Product object
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;   // store the Product object reference
        this.quantity = quantity;
    }
}
```

#### 💻 Snippet 2 — Delegation + Setter
```java
    // Delegation: asks the Product for its price — doesn't store it separately
    public double getTotalPrice() {
        return product.getPrice() * quantity;
        // e.g., Laptop ($50000) x 2 = $100000
    }

    // Only setter: quantity can be updated when same item is added again
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getters — read-only access to fields
    public Product getProduct()  { return product; }
    public int getQuantity()     { return quantity; }
```

> 🔑 **How it works:**
> - `CartItem` doesn't copy price/name from `Product` — it holds the actual `Product` object
> - If `product.getPrice()` ever changed (in a more advanced version), `getTotalPrice()` would automatically reflect it
> - `setQuantity()` is allowed because the same product can be added multiple times — quantity increases
> - This is **Composition**: `CartItem` is built *from* a `Product`, not a subtype of it

---

### 9. `CartService.java` — Cart Interface

#### 🧩 Concepts Used
- **Interface** — contract for all cart operations, no implementation
- **Exception Declaration** — `checkout()` declares `throws PaymentException`
- **Return Types** — `boolean` signals success/failure to the caller
- **Abstraction** — hides all cart storage and logic details

#### 📌 Key Points
- 6 methods defined: `addToCart`, `removeFromCart`, `viewCart`, `calculateTotal`, `checkout`, `isCartEmpty`
- `checkout()` is the only method that declares a checked exception — payment failure is critical
- `isCartEmpty()` is a utility method — used to guard against checking out with nothing in the cart
- `calculateTotal()` returns `double` — the grand total of all items in cart

#### 💻 Snippet 1 — Full Interface
```java
public interface CartService {

    void addToCart(Product product, int quantity);  // add item to cart

    void removeFromCart(int productId);             // remove by product ID

    void viewCart();                                // display formatted cart

    double calculateTotal();                        // sum of all item totals

    // Declares checked exception — caller MUST handle PaymentException
    boolean checkout(double amountEntered) throws PaymentException;

    boolean isCartEmpty();                          // true if cart has no items
}
```

> 🔑 **How it works:**
> - The `throws PaymentException` on `checkout()` means any code calling this method must use `try-catch` — Java enforces it at compile time
> - `isCartEmpty()` returns `boolean` — used in `ECommerceApp` to prevent empty-cart checkout attempts
> - Separating the interface from implementation means you could replace `CartServiceImpl` with a `DatabaseCartService` without touching calling code

---

### 10. `CartServiceImpl.java` — Cart Logic

#### 🧩 Concepts Used
- **Collections** — `ArrayList<CartItem>` stores the cart items
- **Exception Handling** — throws `PaymentException` for invalid payment
- **Interface Implementation** — implements all 6 `CartService` methods
- **Formatted Output** — `printf` for aligned cart display
- **Wrapper Classes** — `Double` implicitly used in `String.format()`

#### 📌 Key Points
- `addToCart()` first checks if the product already exists in the cart — if yes, increments quantity; if no, adds new `CartItem`
- `removeFromCart()` does NOT throw an exception on missing ID — it just prints a message (removal failure is non-critical)
- `viewCart()` uses `printf` to format a table with ID, name, quantity, and total price per item
- `calculateTotal()` sums up `item.getTotalPrice()` across all items in the `ArrayList`
- `checkout()` throws `PaymentException` if amount ≤ 0, prints error if underpaid or overpaid, clears cart on exact match
- `cart.clear()` on success empties the `ArrayList` — equivalent to placing the order

#### 💻 Snippet 1 — addToCart (Duplicate Check + Add)
```java
private List<CartItem> cart = new ArrayList<>();  // Collection: holds cart items

@Override
public void addToCart(Product product, int quantity) {
    // Check if product already in cart — update quantity if so
    for (CartItem item : cart) {
        if (item.getProduct().getId() == product.getId()) {
            item.setQuantity(item.getQuantity() + quantity);  // increment
            System.out.printf("  ✔ Updated: %-20s → Qty: %d%n", product.getName(), item.getQuantity());
            return;  // stop here — no need to add again
        }
    }
    // Product not in cart yet — add as new CartItem
    cart.add(new CartItem(product, quantity));
    System.out.printf("  ✔ Added: %-20s x%d to cart.%n", product.getName(), quantity);
}
```

#### 💻 Snippet 2 — removeFromCart
```java
@Override
public void removeFromCart(int productId) {
    for (CartItem item : cart) {
        if (item.getProduct().getId() == productId) {
            System.out.println("  ✔ Removed: " + item.getProduct().getName());
            cart.remove(item);   // ArrayList.remove() — removes the CartItem object
            return;
        }
    }
    // No exception thrown — just informational message (non-critical failure)
    System.out.println("  ✘ Product ID " + productId + " not found in cart.");
}
```

#### 💻 Snippet 3 — viewCart (Formatted Table Output)
```java
@Override
public void viewCart() {
    if (cart.isEmpty()) {   // ArrayList.isEmpty()
        System.out.println("  Your cart is empty.");
        return;
    }

    System.out.printf("  %-6s %-22s %-6s %10s%n", "ID", "Product", "Qty", "Total");
    for (CartItem item : cart) {
        System.out.printf("  [%3d] %-22s x%-4d $%8.2f%n",
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getTotalPrice());   // delegates to CartItem.getTotalPrice()
    }
    System.out.printf("  %-33s $%8.2f%n", "TOTAL:", calculateTotal());
}
```

#### 💻 Snippet 4 — checkout (Exception Handling)
```java
@Override
public boolean checkout(double amountEntered) throws PaymentException {
    // Exception Handling: invalid input → throw checked exception
    if (amountEntered <= 0) {
        throw new PaymentException("Payment amount must be positive. Entered: $" + amountEntered);
    }

    double total = calculateTotal();

    if (amountEntered == total) {
        System.out.println("  ✔ Payment received. Order confirmed! 🎉");
        cart.clear();   // ArrayList.clear() — empties cart after successful payment
        return true;
    } else if (amountEntered < total) {
        System.out.printf("  ✘ Insufficient. Entered $%.2f but due is $%.2f%n", amountEntered, total);
    } else {
        System.out.printf("  ✘ Overpayment. Please enter exactly $%.2f%n", total);
    }
    return false;
}
```

> 🔑 **How it works:**
> - `ArrayList<CartItem>` grows/shrinks as items are added/removed
> - `addToCart()` avoids duplicate entries — finds and updates instead of adding a second entry
> - `checkout()` validates amount first, throws `PaymentException` for zero/negative values, then checks for exact match
> - `cart.clear()` after payment mirrors a real system where the cart is emptied after order placement
> - `removeFromCart()` uses a `return` after removing — avoids `ConcurrentModificationException` by stopping iteration immediately

---

### 11. `InvalidUserException.java` — Checked Exception

#### 🧩 Concepts Used
- **Custom Exception** — extends Java's built-in `Exception` class
- **Inheritance** — `InvalidUserException` IS-A `Exception` via `extends`
- **Checked Exception** — compiler forces all callers to handle or declare it
- **`super()` call** — passes the message up to `Exception`'s constructor

#### 📌 Key Points
- Extends `Exception` (not `RuntimeException`) → this makes it a **checked** exception
- The compiler will not let you call `signup()` without a `try-catch` or `throws` declaration
- `super(message)` stores the message in the parent `Exception` — retrievable via `e.getMessage()`
- Used in: `UserServiceImpl.signup()` for username/password/email/mobile validation failures

#### 💻 Snippet 1 — Class Definition
```java
// Inheritance: extends Exception → makes this a CHECKED exception
public class InvalidUserException extends Exception {

    public InvalidUserException(String message) {
        super(message);   // passes message to Exception's constructor
        // Caller can later do: e.getMessage() → "Username cannot be empty."
    }
}
```

#### 💻 Snippet 2 — How It Is Thrown and Caught
```java
// THROWING (in UserServiceImpl):
private void validateUsername(String username) throws InvalidUserException {
    if (username.length() < 3)
        throw new InvalidUserException("Username must be at least 3 characters.");
}

// CATCHING (in ECommerceApp / calling code):
try {
    userService.signup("Al", "pass123", "al@gmail.com", "9876543210");
} catch (InvalidUserException e) {
    System.out.println("  ✘ Signup failed: " + e.getMessage());
    // prints: ✘ Signup failed: Username must be at least 3 characters.
}
```

> 🔑 **How it works:**
> - `extends Exception` = checked → Java compiler enforces handling at the call site
> - `super(message)` stores the error message in the parent class — you retrieve it with `e.getMessage()`
> - Every specific validation failure gets its own descriptive message, making debugging easy
> - If you forget the `try-catch`, the code won't compile — this is the key difference from unchecked exceptions

---

### 12. `InvalidProductException.java` — Unchecked Exception

#### 🧩 Concepts Used
- **Custom Exception** — extends `RuntimeException`
- **Inheritance** — `InvalidProductException` IS-A `RuntimeException`
- **Unchecked Exception** — compiler does NOT force handling; optional `try-catch`
- **`super()` call** — message passed to `RuntimeException`

#### 📌 Key Points
- Extends `RuntimeException` → **unchecked** — callers don't have to use `try-catch`
- Thrown by `ProductServiceImpl.getProductById()` when an ID doesn't exist
- Used for "programmer errors" or invalid input that shouldn't normally happen
- If uncaught, it will crash the program with a stack trace — which is acceptable for unexpected states

#### 💻 Snippet 1 — Class Definition
```java
// Inheritance: extends RuntimeException → UNCHECKED exception
// Compiler does NOT force callers to handle this
public class InvalidProductException extends RuntimeException {

    public InvalidProductException(String message) {
        super(message);   // same pattern — message stored in RuntimeException
    }
}
```

#### 💻 Snippet 2 — Thrown vs Checked Exception (Comparison)
```java
// THROWING (in ProductServiceImpl):
public Product getProductById(int id) throws InvalidProductException {
    for (Product p : products) {
        if (p.getId() == id) return p;
    }
    throw new InvalidProductException("No product found with ID: " + id);
}

// CATCHING — optional, but good practice:
try {
    Product p = productService.getProductById(999);
} catch (InvalidProductException e) {
    System.out.println("  ✘ " + e.getMessage());
    // prints: ✘ No product found with ID: 999
}

// WITHOUT try-catch — also valid (unchecked), but will crash if ID is invalid:
Product p = productService.getProductById(999);  // compiles fine, crashes at runtime
```

> 🔑 **How it works:**
> - `extends RuntimeException` — the compiler doesn't force `try-catch` around calls to `getProductById()`
> - Still a good practice to catch it — but Java won't stop you from skipping the catch block
> - Throwing this is better than returning `null` — a `null` can silently cause `NullPointerException` 10 lines later

---

### 13. `PaymentException.java` — Checked Exception

#### 🧩 Concepts Used
- **Custom Exception** — extends `Exception`
- **Inheritance** — `PaymentException` IS-A `Exception`
- **Checked Exception** — compiler forces handling at the call site
- **Domain-Specific Exception** — named after the business operation it protects

#### 📌 Key Points
- Same structure as `InvalidUserException` — extends `Exception` → checked
- Thrown only inside `CartServiceImpl.checkout()` when payment amount is ≤ 0
- Calling code (in `ECommerceApp`) must wrap `checkout()` in a `try-catch (PaymentException e)`
- Makes the payment flow explicit — the method signature itself tells you "this can fail"

#### 💻 Snippet 1 — Class Definition
```java
// Inheritance: extends Exception → CHECKED exception
public class PaymentException extends Exception {

    public PaymentException(String message) {
        super(message);   // stores message in Exception
    }
}
```

#### 💻 Snippet 2 — Thrown and Caught in Context
```java
// THROWING (in CartServiceImpl.checkout):
if (amountEntered <= 0) {
    throw new PaymentException("Payment amount must be positive. Entered: $" + amountEntered);
}

// CATCHING (in ECommerceApp):
try {
    boolean success = cartService.checkout(amountEntered);
    if (success) System.out.println("  ✔ Order placed!");
} catch (PaymentException e) {
    System.out.println("  ✘ Payment error: " + e.getMessage());
    // prints: ✘ Payment error: Payment amount must be positive. Entered: $-50.0
}
```

#### 💻 Snippet 3 — Checked vs Unchecked Comparison
```java
// CHECKED (InvalidUserException, PaymentException — extends Exception):
// → Compiler ERROR if you don't handle it:
userService.signup(...);            // ❌ compile error — must add try-catch or throws
cartService.checkout(amount);       // ❌ compile error — must add try-catch or throws

// UNCHECKED (InvalidProductException — extends RuntimeException):
// → Compiler is SILENT, but crashes at runtime if not handled:
productService.getProductById(999); // ✅ compiles — but crashes if ID not found and not caught
```

> 🔑 **How it works:**
> - Three custom exceptions follow the same structure but differ in what they extend
> - `extends Exception` = checked = compiler enforced = use for critical business operations (signup, payment)
> - `extends RuntimeException` = unchecked = optional handling = use for programming errors (invalid ID lookup)
> - All three use `super(message)` to store a descriptive message readable via `e.getMessage()`

---

## 🧠 Concepts Summary

### OOP Concepts
- **Encapsulation** → `User`, `Product`, `CartItem` — `private` fields, `public` getters, no direct field access
- **Interface** → `UserService`, `ProductService`, `CartService` — contract-based design, separates what from how
- **Interface Implementation** → `UserServiceImpl`, `ProductServiceImpl`, `CartServiceImpl` — `implements` keyword, `@Override` on every method
- **Object Composition** → `CartItem` HAS-A `Product` — not inheritance, but embedding one object inside another
- **Enum** → `Category` — fixed constants `ELECTRONICS`, `FOOD`, `CLOTHING`, `SPORTS` with a `displayName()` method

### Collections
- **ArrayList** → used in all 3 `*ServiceImpl` classes — stores `User`, `Product`, and `CartItem` objects
- **List interface** → return type of `getAvailableCategories()` and field type of all lists
- **Stream API** → `ProductServiceImpl.displayByCategory()` — `.stream().filter(lambda).collect()`
- **Lambda** → `p -> p.getCategory() == category` — inline function passed to `.filter()`
- **Arrays.asList()** → wraps `Category.values()` enum array into a fixed `List`
- **ArrayList methods used** → `.add()`, `.remove()`, `.clear()`, `.isEmpty()`, enhanced for-loop

### Exception Handling
- **Custom Checked Exception** → `InvalidUserException`, `PaymentException` — extend `Exception`, compiler enforced
- **Custom Unchecked Exception** → `InvalidProductException` — extends `RuntimeException`, optional handling
- **`throw`** → used inside methods to trigger an exception immediately
- **`throws`** → declared on method signatures to warn callers of possible exceptions
- **`try-catch`** → used in calling code to handle checked exceptions gracefully
- **`super(message)`** → passes the error message to the parent exception class
- **`e.getMessage()`** → retrieves the stored message from a caught exception

### Wrapper Classes & Other
- **`String`** → wrapper class used throughout; methods like `.equals()`, `.equalsIgnoreCase()`, `.trim()`, `.matches()`, `.indexOf()`, `.substring()`
- **`Double`** → implicitly used in `String.format("%.2f", total)`
- **`printf` formatting** → `%2d`, `%-20s`, `%8.2f`, `%n` used for aligned console output
- **`this` keyword** → used in constructors to distinguish field from parameter
- **`@Override`** → annotation confirming interface method is correctly implemented
- **Regex** → `"\\d{10}"` in `validateMobile()` — matches exactly 10 numeric digits

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
