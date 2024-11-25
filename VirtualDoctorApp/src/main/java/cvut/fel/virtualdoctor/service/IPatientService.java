package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.model.Patient;

public interface IPatientService {

    Patient createPatient(Patient patient);

    Patient findByName(String name);
}
