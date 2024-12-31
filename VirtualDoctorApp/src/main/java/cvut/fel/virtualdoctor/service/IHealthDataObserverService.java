package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.model.HealthData;

public interface IHealthDataObserverService {

    /**
     * Update vital signs in database.
     *
     * @param healthData vital signs from subject (data source like smartwatch, mobile app etc.)
     */
    void update(HealthData healthData);

    /**
     * Provide vital signs in needed format for other services e.g. EvaluatorService to evaluate
     * patient's health status.
     *
     * @param patient name for which vital signs are needed
     * @return vital signs in needed format
     */
    HealthData provideHealthData(Patient patient);
}
