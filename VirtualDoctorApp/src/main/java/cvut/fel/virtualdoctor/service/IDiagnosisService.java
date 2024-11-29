package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.classifier.client.ClassifierOutput;
import cvut.fel.virtualdoctor.model.Diagnosis;
import cvut.fel.virtualdoctor.model.PatientInput;

public interface IDiagnosisService {

    Diagnosis createDiagnosis(PatientInput patientInput, ClassifierOutput classifierOutput);
    void save(Diagnosis diagnosis);
}
