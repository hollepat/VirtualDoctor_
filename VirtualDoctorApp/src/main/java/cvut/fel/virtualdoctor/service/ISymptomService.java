package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.SymptomDTO;
import cvut.fel.virtualdoctor.model.Symptom;

public interface ISymptomService {

    void addSymptom(Symptom symptom);
    void deleteSymptom(SymptomDTO symptomDTO);
}
