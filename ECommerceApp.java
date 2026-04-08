package E_CommerceWebsite;

import java.util.Scanner;

public class ECommerceApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserServiceImpl();
        ProductService productService = new ProductServiceImpl();
        CartService cartService = new CartServiceImpl();

        printBanner();

        boolean exit = false;
        while (!exit) {
            System.out.println("\n  ┌─────────────────────────┐");
            System.out.println("  │  1. Sign Up             │");
            System.out.println("  │  2. Log In              │");
            System.out.println("  │  3. Exit                │");
            System.out.println("  └─────────────────────────┘");
            System.out.print("  Choose: ");

            int choice = readInt(sc);
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("\n  Username : ");
                    String su = sc.nextLine().trim();
                    System.out.print("  Password : ");
                    String sp = sc.nextLine().trim();
                    System.out.print("  Email    : ");
                    String se = sc.nextLine().trim();
                    userService.signup(su, sp, se);
                    break;

                case 2:
                    System.out.print("\n  Username : ");
                    String lu = sc.nextLine().trim();
                    System.out.print("  Password : ");
                    String lp = sc.nextLine().trim();
                    boolean loggedIn = userService.login(lu, lp);

                    if (loggedIn) {
                        runUserMenu(sc, productService, cartService);
                    }
                    break;

                case 3:
                    exit = true;
                    System.out.println("\n  Thank you for shopping! Goodbye. 👋\n");
                    break;

                default:
                    System.out.println("  ✘ Invalid choice. Try again.");
            }
        }
        sc.close();
    }

    // ──────────────────────────────────────────────────────────────
    private static void runUserMenu(Scanner sc, ProductService productService, CartService cartService) {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n  ┌─────────────────────────────┐");
            System.out.println("  │  1. Browse Products         │");
            System.out.println("  │  2. Add Item to Cart        │");
            System.out.println("  │  3. Remove Item from Cart   │");
            System.out.println("  │  4. View Cart               │");
            System.out.println("  │  5. Proceed to Payment      │");
            System.out.println("  │  6. Logout                  │");
            System.out.println("  └─────────────────────────────┘");
            System.out.print("  Choose: ");

            int option = readInt(sc);

            switch (option) {
                case 1:
                    productService.displayProducts();
                    break;

                case 2:
                    productService.displayProducts();
                    System.out.print("\n  Enter Product ID to add: ");
                    int pid = readInt(sc);
                    System.out.print("  Enter Quantity          : ");
                    int qty = readInt(sc);
                    Product product = productService.getProductById(pid);
                    if (product == null) {
                        System.out.println("  ✘ Product not found.");
                    } else if (qty <= 0) {
                        System.out.println("  ✘ Quantity must be at least 1.");
                    } else {
                        cartService.addToCart(product, qty);
                    }
                    break;

                case 3:
                    // Show cart first so user knows what IDs to enter
                    if (cartService.isCartEmpty()) {
                        System.out.println("\n  Cart is already empty. Nothing to remove.");
                    } else {
                        cartService.viewCart();
                        System.out.print("\n  Enter Product ID to remove: ");
                        int rid = readInt(sc);
                        cartService.removeFromCart(rid);
                    }
                    break;

                case 4:
                    cartService.viewCart();
                    break;

                case 5:
                    handlePayment(sc, cartService);
                    break;

                case 6:
                    logout = true;
                    System.out.println("\n  Logged out successfully. See you soon!");
                    break;

                default:
                    System.out.println("  ✘ Invalid option. Please try again.");
            }
        }
    }

    // ──────────────────────────────────────────────────────────────
    private static void handlePayment(Scanner sc, CartService cartService) {
        if (cartService.isCartEmpty()) {
            System.out.println("\n  ✘ Your cart is empty. Add items before proceeding to payment.");
            return;
        }

        // Show cart summary
        cartService.viewCart();

        double total = cartService.calculateTotal();

        System.out.println("\n  ════════════ CHECKOUT ════════════");
        System.out.printf("  Amount Due  :  $%.2f%n", total);
        System.out.println("  ──────────────────────────────────");
        System.out.println("  Payment Method: Cash on Delivery");
        System.out.println("  ──────────────────────────────────");
        System.out.printf("  Enter amount to pay ($%.2f): ", total);

        double amountPaid = readDouble(sc);

        System.out.println("\n  Processing payment...");
        pause(800);

        boolean success = cartService.checkout(amountPaid);

        if (!success) {
            System.out.println("  Payment failed. Your cart is unchanged. Please try again.");
        }

        System.out.println("  ══════════════════════════════════");
    }

    // ──────────────────────────────────────────────────────────────
    //  Utility helpers
    // ──────────────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println("\n  ╔════════════════════════════════════╗");
        System.out.println("  ║     Welcome to ShopEasy 🛒          ║");
        System.out.println("  ║   Your One-Stop E-Commerce Store    ║");
        System.out.println("  ╚════════════════════════════════════╝");
    }

    private static int readInt(Scanner sc) {
        try {
            int val = sc.nextInt();
            return val;
        } catch (Exception e) {
            sc.nextLine(); // clear bad input
            return -1;
        }
    }

    private static double readDouble(Scanner sc) {
        try {
            double val = sc.nextDouble();
            return val;
        } catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }

    private static void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}