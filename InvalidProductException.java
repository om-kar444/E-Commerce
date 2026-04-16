
package E_CommerceWebsite2;

// OOP: Custom Exception classes using Inheritance (extends Exception / RuntimeException)
// Access Modifier: public — usable across all packages

// ─── Exception Handling: thrown when a product is not found ───
public class InvalidProductException extends RuntimeException {
    // Inheritance: extends RuntimeException (unchecked exception)
    public InvalidProductException(String message) {
        super(message); // calls parent constructor
    }
}
