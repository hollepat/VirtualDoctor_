package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.model.Symptom;

public interface ISymptomService {

    void addSymptom(Symptom symptom);
    void deleteSymptom(Symptom symptom);
}
