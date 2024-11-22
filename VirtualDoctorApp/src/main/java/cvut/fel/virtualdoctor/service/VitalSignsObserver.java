package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.VitalSignsDTO;
import cvut.fel.virtualdoctor.model.VitalSigns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VitalSignsObserver implements IVitalSignsObserver {

    private final Logger logger = LoggerFactory.getLogger(VitalSignsObserver.class);

    @Override
    public void update(VitalSignsDTO vitalSignsDTO) {
        logger.info("Vital signs updated: " + vitalSignsDTO.toString());
    }

    @Override
    public VitalSigns provideVitalSigns() {
        return null;
    }
}
