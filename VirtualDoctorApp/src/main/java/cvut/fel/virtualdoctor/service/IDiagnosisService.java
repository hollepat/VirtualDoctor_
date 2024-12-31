package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.classifier.client.ClassifierOutputDTO;
import cvut.fel.virtualdoctor.model.ClassifierInput;
import cvut.fel.virtualdoctor.model.Diagnosis;
import cvut.fel.virtualdoctor.model.PatientInput;

public interface IDiagnosisService {

    Diagnosis createDiagnosis(ClassifierInput classifierInput, PatientInput patientInput, ClassifierOutputDTO classifierOutputDTO);
    void save(Diagnosis diagnosis);
}
