package cvut.fel.virtualdoctor.model;

import java.util.Comparator;

public enum EmergencyType {
    NORMAL(1),  // This indicates a condition that does not require urgent medical attention.
    STAY_AT_HOME(2),   // The condition is not immediately life-threatening, but the patient should be monitored at home with appropriate care and treatment.
    LIFE_THREATENING(3);    // This category indicates a critical condition where immediate medical intervention is necessary to preserve life.

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
