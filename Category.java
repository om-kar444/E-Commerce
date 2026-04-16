package E_CommerceWebsite2;

// OOP: Enum acts as a special class for fixed category constants
// Access Modifier: public so all classes can use it
public enum Category {
    ELECTRONICS,   // Category 1 - Electronic gadgets
    FOOD,          // Category 2 - Food items
    CLOTHING,      // Category 3 - Clothes and apparel
    SPORTS;        // Category 4 - Sports equipment

    // Wrapper class usage: Integer.toString wraps int display index
    public String displayName() {
        // Returns a human-friendly name for the category
        switch (this) {
            case ELECTRONICS:
                return "Electronics";
            case FOOD:
                return "Food";
            case CLOTHING:
                return "Clothing";
            case SPORTS:
                return "Sports";
            default:
                return this.name();
        }
    }
}