package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.VitalSignsDTO;
import cvut.fel.virtualdoctor.model.VitalSigns;

public interface IVitalSignsObserver {

    /**
     * Update vital signs in database.
     *
     * @param vitalSignsDTO vital signs from subject (data source like smartwatch, mobile app etc.)
     */
    void update(VitalSignsDTO vitalSignsDTO);

    /**
     * Provide vital signs in needed format for other services e.g. EvaluatorService to evaluate
     * patient's health status.
     *
     * @return vital signs in needed format
     */
    VitalSigns provideVitalSigns();
}