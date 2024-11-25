package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.model.VitalSigns;

public interface IVitalSignsObserver {

    /**
     * Update vital signs in database.
     *
     * @param vitalSigns vital signs from subject (data source like smartwatch, mobile app etc.)
     */
    void update(VitalSigns vitalSigns);

    /**
     * Provide vital signs in needed format for other services e.g. EvaluatorService to evaluate
     * patient's health status.
     *
     * @param patient name for which vital signs are needed
     * @return vital signs in needed format
     */
    VitalSigns provideVitalSigns(Patient patient);
}
