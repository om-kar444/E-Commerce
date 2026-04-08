package E_CommerceWebsite;

public interface UserService {
    boolean signup(String username, String password, String se);
    boolean login(String username, String password);
}