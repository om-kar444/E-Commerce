package E_CommerceWebsite2;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ECommerceApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        UserService    userService    = new UserServiceImpl();
        ProductService productService = new ProductServiceImpl();
        CartService    cartService    = new CartServiceImpl();

        printBanner();

        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int choice = readInt(sc);

            switch (choice) {
                case 1:
                    handleSignup(sc, userService);
                    break;
                case 2:
                    handleLogin(sc, userService, productService, cartService);
                    break;
                case 3:
                    exit = true;
                    System.out.println("\n  Thank you for visiting ShopEasy! Goodbye. 👋\n");
                    break;
                default:
                    System.out.println("  ✘ Invalid choice. Please select 1–3.");
            }
        }
        sc.close();
    }

    // ── SIGNUP HANDLER ──────────────────────────────────────────────────────────
    private static void handleSignup(Scanner sc, UserService userService) {
        System.out.println("\n  ╔══════════ SIGN UP ══════════╗");
        sc.nextLine();

        System.out.print("  Username     : ");
        String username = sc.nextLine().trim();

        System.out.print("  Password     : ");
        String password = sc.nextLine().trim();

        System.out.print("  Email        : ");
        String email = sc.nextLine().trim();

        System.out.print("  Mobile (10d) : ");
        String mobile = sc.nextLine().trim();

        try {
            userService.signup(username, password, email, mobile);
        } catch (InvalidUserException e) {
            System.out.println("  ✘ Signup failed: " + e.getMessage());
        }
    }

    // ── LOGIN HANDLER ────────────────────────────────────────────────────────────
    private static void handleLogin(Scanner sc, UserService userService,
                                    ProductService productService, CartService cartService) {
        System.out.println("\n  ╔══════════ LOG IN ══════════╗");
        sc.nextLine();

        System.out.print("  Username : ");
        String username = sc.nextLine().trim();
        System.out.print("  Password : ");
        String password = sc.nextLine().trim();

        boolean loggedIn = userService.login(username, password);
        if (loggedIn) {
            runUserMenu(sc, productService, cartService);
        }
    }

    // ── USER MENU ────────────────────────────────────────────────────────────────
    private static void runUserMenu(Scanner sc, ProductService productService,
                                    CartService cartService) {
        boolean logout = false;

        while (!logout) {
            printUserMenu();
            int option = readInt(sc);

            switch (option) {

                case 1:
                    // Show category list first, then products in chosen category
                    handleBrowseByCategory(sc, productService);
                    break;

                case 2:
                    // Same — category first, then products
                    handleBrowseByCategory(sc, productService);
                    break;

                case 3:
                    handleAddToCart(sc, productService, cartService);
                    break;

                case 4:
                    if (cartService.isCartEmpty()) {
                        System.out.println("\n  Cart is empty — nothing to remove.");
                    } else {
                        cartService.viewCart();
                        System.out.print("\n  Enter Product ID to remove: ");
                        int rid = readInt(sc);
                        cartService.removeFromCart(rid);
                    }
                    break;

                case 5:
                    cartService.viewCart();
                    break;

                case 6:
                    handlePayment(sc, cartService);
                    break;

                case 7:
                    logout = true;
                    System.out.println("\n  Logged out successfully. See you soon! 👋");
                    break;

                default:
                    System.out.println("  ✘ Invalid option. Please select 1–7.");
            }
        }
    }

    // ── BROWSE BY CATEGORY ───────────────────────────────────────────────────────
    private static void handleBrowseByCategory(Scanner sc, ProductService productService) {
        System.out.println("\n  ─── Select a Category ─────────────────────");
        System.out.println("  0. Show All Products");
        int i = 1;
        for (Category cat : productService.getAvailableCategories()) {
            System.out.println("  " + i + ". " + cat.displayName());
            i++;
        }
        System.out.println("  ───────────────────────────────────────────");
        System.out.print("  Enter choice: ");
        int catChoice = readInt(sc);

        if (catChoice == 0) {
            productService.displayAllProducts();
            return;
        }

        Category[] categories = Category.values();
        if (catChoice < 1 || catChoice > categories.length) {
            System.out.println("  ✘ Invalid category choice.");
            return;
        }
        productService.displayByCategory(categories[catChoice - 1]);
    }

    // ── ADD TO CART ──────────────────────────────────────────────────────────────
    private static void handleAddToCart(Scanner sc, ProductService productService,
                                        CartService cartService) {
        System.out.println("\n  ─── Browse Products ───────────────────────");
        System.out.println("  0. Show All Products");
        int i = 1;
        for (Category cat : productService.getAvailableCategories()) {
            System.out.println("  " + i + ". " + cat.displayName());
            i++;
        }
        System.out.println("  ───────────────────────────────────────────");
        System.out.print("  Select category (or 0 for all): ");
        int catChoice = readInt(sc);

        if (catChoice == 0) {
            productService.displayAllProducts();
        } else {
            Category[] categories = Category.values();
            if (catChoice < 1 || catChoice > categories.length) {
                System.out.println("  ✘ Invalid category choice.");
                return;
            }
            productService.displayByCategory(categories[catChoice - 1]);
        }

        System.out.print("\n  Enter Product ID to add: ");
        int pid = readInt(sc);
        System.out.print("  Enter Quantity          : ");
        int qty = readInt(sc);

        try {
            Product product = productService.getProductById(pid);
            if (qty <= 0) {
                System.out.println("  ✘ Quantity must be at least 1.");
            } else {
                cartService.addToCart(product, qty);
            }
        } catch (InvalidProductException e) {
            System.out.println("  ✘ " + e.getMessage());
        }
    }

    // ── PAYMENT HANDLER ──────────────────────────────────────────────────────────
    private static void handlePayment(Scanner sc, CartService cartService) {
        if (cartService.isCartEmpty()) {
            System.out.println("\n  ✘ Cart is empty. Add items before paying.");
            return;
        }

        cartService.viewCart();
        double total = cartService.calculateTotal();

        System.out.println("\n  ════════════════ CHECKOUT ════════════════");
        System.out.printf("  Amount Due       : $%.2f%n", total);
        System.out.println("  Payment Method   : Cash on Delivery");
        System.out.println("  ──────────────────────────────────────────");
        System.out.printf("  Enter amount ($%.2f): ", total);

        double amountPaid = readDouble(sc);

        System.out.println("\n  Processing payment...");
        pause(800);

        try {
            boolean success = cartService.checkout(amountPaid);
            if (!success) {
                System.out.println("  Payment failed. Cart unchanged. Please try again.");
            }
        } catch (PaymentException e) {
            System.out.println("  ✘ Payment Error: " + e.getMessage());
        }

        System.out.println("  ══════════════════════════════════════════");
    }

    // ── UI HELPERS ───────────────────────────────────────────────────────────────
    private static void printBanner() {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║        🛒  Welcome to ShopEasy            ║");
        System.out.println("  ║     Your One-Stop E-Commerce Store        ║");
        System.out.println("  ║   Electronics | Food | Clothing | Sports  ║");
        System.out.println("  ╚══════════════════════════════════════════╝\n");
    }

    private static void printMainMenu() {
        System.out.println("\n  ┌─────────────────────────┐");
        System.out.println("  │     MAIN MENU           │");
        System.out.println("  │  1. Sign Up             │");
        System.out.println("  │  2. Log In              │");
        System.out.println("  │  3. Exit                │");
        System.out.println("  └─────────────────────────┘");
        System.out.print("  Choose: ");
    }

    private static void printUserMenu() {
        System.out.println("\n  ┌──────────────────────────────┐");
        System.out.println("  │        SHOP MENU             │");
        System.out.println("  │  1. Browse All Products      │");
        System.out.println("  │  2. Browse by Category       │");
        System.out.println("  │  3. Add Item to Cart         │");
        System.out.println("  │  4. Remove Item from Cart    │");
        System.out.println("  │  5. View Cart                │");
        System.out.println("  │  6. Proceed to Payment       │");
        System.out.println("  │  7. Logout                   │");
        System.out.println("  └──────────────────────────────┘");
        System.out.print("  Choose: ");
    }

    private static int readInt(Scanner sc) {
        try {
            int val = sc.nextInt();
            return val;
        } catch (InputMismatchException e) {
            sc.nextLine();
            return -1;
        }
    }

    private static double readDouble(Scanner sc) {
        try {
            double val = sc.nextDouble();
            return val;
        } catch (InputMismatchException e) {
            sc.nextLine();
            return -1.0;
        }
    }

    private static void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
