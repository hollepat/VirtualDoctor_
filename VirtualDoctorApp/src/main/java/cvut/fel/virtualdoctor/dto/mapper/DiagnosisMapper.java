package cvut.fel.virtualdoctor.dto.mapper;

import cvut.fel.virtualdoctor.dto.DiagnosisDTO;
import cvut.fel.virtualdoctor.model.Diagnosis;
import cvut.fel.virtualdoctor.model.DifferentialList;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DiagnosisMapper {

    private final int resultsLimit;

    public DiagnosisMapper(int resultsLimit) {
        this.resultsLimit = resultsLimit;
    }

    public DiagnosisDTO toDTO(Diagnosis diagnosis) {
        return new DiagnosisDTO(
            diagnosis.getSwVersion(),
            diagnosis.getTimeAndDate(),
            diagnosis.getDoctorToVisit(),
            getBestPredictions(diagnosis.getDifferentialList()),
            diagnosis.getEmergency()
        );
    }

    public Map<String, Double> getBestPredictions(DifferentialList ddx) {
        // Sort the map by values in descending order
        return ddx.getDdx()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(this.resultsLimit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // Handle value conflicts if necessary
                        LinkedHashMap::new // Preserve insertion order
                ));
    }
}
