package E_CommerceWebsite;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private List<User> users = new ArrayList<User>();

    public boolean signup(String username, String password, String email) {
        for(User u : users) {
            if(u.getUsername().equals(username)) {
                System.out.println("Username already exists!");
                return false;
            }
        }
        users.add(new User(username, password, email));
        System.out.println("Signup successful!");
        return true;
    }

    public boolean login(String username, String password) {
        for(User u : users) {
            if(u.getUsername().equals(username) && u.getPassword().equals(password)) {
                System.out.println("Login successful!");
                System.out.println("Welcome, " + u.getUsername() + " (" + u.getEmail() + ")");
                return true;
            }
        }
        System.out.println("Invalid username or password!");
        return false;
    }
}