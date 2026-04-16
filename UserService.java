

package E_CommerceWebsite2;

// OOP: Interface — defines the contract for user operations
// Access Modifier: public interface
public interface UserService {

    // Exception Handling: throws checked InvalidUserException for validation failures
    boolean signup(String username, String password, String email, String mobile)
            throws InvalidUserException;

    boolean login(String username, String password);
}
