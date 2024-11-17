package cvut.fel.virtualdoctor.classifier.client;

import cvut.fel.virtualdoctor.model.Gender;
import cvut.fel.virtualdoctor.model.Lifestyle;
import cvut.fel.virtualdoctor.model.Location;

import java.util.List;
import java.util.Map;

public record ClassifierInput(
        int age,
        int height,
        int weight,
        Lifestyle lifestyle,
        Gender gender,
        Location location,
        List<String> symptoms,
        Map<String, Double> vitalSigns
) {
}