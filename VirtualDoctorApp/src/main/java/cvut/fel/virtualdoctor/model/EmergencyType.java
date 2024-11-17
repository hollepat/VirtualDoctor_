package cvut.fel.virtualdoctor.model;

import java.util.Comparator;

public enum EmergencyType {
    NORMAL(1),  // No immediate action is required
    STAY_AT_HOME(2),   // The patient should stay at home and rest
    LIFE_THREATENING(3);    // The patient should be seen immediately

    private final int level;

    EmergencyType(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    // Custom comparator for sorting
    public static final Comparator<EmergencyType> LEVEL_COMPARATOR = Comparator.comparingInt(EmergencyType::getLevel);
}
