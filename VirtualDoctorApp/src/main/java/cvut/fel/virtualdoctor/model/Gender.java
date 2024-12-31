package cvut.fel.virtualdoctor.model;

public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String displayName;

    // Constructor to set the display name
    Gender(String displayName) {
        this.displayName = displayName;
    }

    // Override toString method to return custom string representation
    @Override
    public String toString() {
        return displayName;
    }

    // If you need to get the display name explicitly
    public String getDisplayName() {
        return displayName;
    }
}
