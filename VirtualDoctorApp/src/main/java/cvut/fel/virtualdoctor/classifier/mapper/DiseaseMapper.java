package cvut.fel.virtualdoctor.classifier.mapper;

import cvut.fel.virtualdoctor.model.Disease;

import java.util.Map;
import java.util.stream.Collectors;

// TODO delete this class
public class DiseaseMapper {

    public static Map<String, Double> mapDiseasesToNames(Map<Disease, Double> ddx) {
        return ddx.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getName(), Map.Entry::getValue));
    }
}
