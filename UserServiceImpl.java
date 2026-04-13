package E_CommerceWebsite2;

import java.util.ArrayList;  // Collections: ArrayList
import java.util.List;        // Collections: List interface

// OOP: Implements UserService interface (Interface + Implementation pattern)
// Inheritance: implements UserService
// Access Modifier: public class
public class UserServiceImpl implements UserService {

    // Collections: List<User> stores all registered users in memory
    // Access Modifier: private — internal implementation detail
    private List<User> users = new ArrayList<>();

    // ── SIGNUP ────────────────────────────────────────────────────
    // Exception Handling: throws InvalidUserException for bad input
    @Override
    public boolean signup(String username, String password, String email, String mobile)
            throws InvalidUserException {

        // Exception Handling: validate each field, throw if invalid
        validateUsername(username);
        validatePassword(password);
        validateEmail(email);       // Email validation: must contain "@" and "."
        validateMobile(mobile);     // Mobile validation: 10-digit number

        // Collections: iterating List with enhanced for-loop
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                // Exception Handling: duplicate username
                throw new InvalidUserException("Username '" + username + "' is already taken!");
            }
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new InvalidUserException("Email '" + email + "' is already registered!");
            }
        }

        // Collections: adding new User object to ArrayList
        users.add(new User(username, password, email, mobile));
        System.out.println("\n  ✔ Signup successful! Welcome, " + username + "!");
        return true;
    }

    // ── LOGIN ─────────────────────────────────────────────────────
    @Override
    public boolean login(String username, String password) {
        // Collections: searching through the list
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                System.out.println("\n  ✔ Login successful!");
                System.out.printf("  Welcome back, %s  |  📧 %s  |  📱 %s%n",
                        u.getUsername(), u.getEmail(), u.getMobileNumber());
                return true;
            }
        }
        System.out.println("\n  ✘ Invalid username or password.");
        return false;
    }

    // ── PRIVATE VALIDATORS ─────────────────────────────────────────
    // Access Modifier: private — helper methods, not exposed via interface

    private void validateUsername(String username) throws InvalidUserException {
        // Wrapper class: String.isEmpty() checks null-like state
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUserException("Username cannot be empty.");
        }
        if (username.length() < 3) {
            throw new InvalidUserException("Username must be at least 3 characters.");
        }
    }

    private void validatePassword(String password) throws InvalidUserException {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidUserException("Password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new InvalidUserException("Password must be at least 6 characters.");
        }
    }

    // Email Validation: must have "@" and a "." after "@", and domain must end with valid TLD
    private void validateEmail(String email) throws InvalidUserException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserException("Email cannot be empty.");
        }

        // Wrapper class: String methods check structure
        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            throw new InvalidUserException("Email must contain '@' (e.g., user@gmail.com).");
        }

        String domain = email.substring(atIndex + 1); // part after "@"
        if (!domain.contains(".")) {
            throw new InvalidUserException("Email domain must contain '.' (e.g., gmail.com).");
        }

        // Domain must end with a valid TLD (.com, .in, .org, etc.) — not just "."
        int lastDot = domain.lastIndexOf('.');
        String tld = domain.substring(lastDot + 1);
        if (tld.isEmpty()) {
            throw new InvalidUserException("Email must have a valid domain ending (e.g., .com, .in).");
        }
    }

    // Mobile Validation: exactly 10 digits, no spaces or special chars
    private void validateMobile(String mobile) throws InvalidUserException {
        if (mobile == null || mobile.trim().isEmpty()) {
            throw new InvalidUserException("Mobile number cannot be empty.");
        }
        // Wrapper class: Long.parseLong wraps long primitive to check if numeric
        if (!mobile.matches("\\d{10}")) {
            throw new InvalidUserException("Mobile number must be exactly 10 digits (e.g., 9876543210).");
        }
    }
}
