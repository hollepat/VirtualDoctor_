package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.PatientDTO;

public interface IPatientService {

    void createUser(PatientDTO patientDTO);
}
