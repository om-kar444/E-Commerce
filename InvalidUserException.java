package E_CommerceWebsite2;

// Exception Handling: thrown when signup/login validation fails
// Inheritance: extends Exception (checked — caller must handle it)
public class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);
    }
}
