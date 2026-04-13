package E_CommerceWebsite2;

// OOP: User model class — encapsulates user data
// Access Modifier: public class, private fields
public class User {

    // Access Modifier: private — Encapsulation (data hiding)
    private String username;
    private String password;
    private String email;
    private String mobileNumber; // NEW: stores 10-digit mobile number

    // Constructor: takes all four fields
    public User(String username, String password, String email, String mobileNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    // Access Modifier: public getters — safe read access
    public String getUsername()     { return username; }
    public String getPassword()     { return password; }
    public String getEmail()        { return email; }
    public String getMobileNumber() { return mobileNumber; }
}