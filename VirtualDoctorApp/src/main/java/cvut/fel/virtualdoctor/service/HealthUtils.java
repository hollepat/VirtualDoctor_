package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.model.Gender;

public class HealthUtils {

    public static String convertCholesterolLevel(double value, int age) {
        if (age < 20) {
            // Child
            return childCholesterolLevel(value);
        } else {
            // Adult
            return adultCholesterolLevel(value);
        }
    }

    private static String adultCholesterolLevel(double value) {
        if (value > 0 && value < 100) {
            return "Low";
        } else if (value >= 100 && value < 160) {
            return "Normal";
        } else if (value >= 160) {
            return "High";
        } else {
            return null;
        }
    }

    private static String childCholesterolLevel(double value) {
        if (value > 0 && value < 240) {
            return "Normal";
        } else if (value >= 240) {
            return "High";
        } else {
            return null;
        }
    }

    public static String convertBloodPressure(double value) {
        if (value > 0 && value <= 90) {
            return "Low";
        } else if (value > 90 && value < 130) {
            return "Normal";
        } else if (value >= 130) {
            return "High";
        } else {
            return null;
        }
    }
}