package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.exception.NotFoundException;
import cvut.fel.virtualdoctor.model.DiseaseDetails;
import cvut.fel.virtualdoctor.repository.DiseaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DiseaseService implements IDiseaseService {

    DiseaseRepository diseaseRepository;

    public DiseaseDetails getDiseaseDetails(String disease) {
        return diseaseRepository.findByName(disease)
                .orElseThrow(() -> new NotFoundException("Disease not found"))
                .getDetails();
    }

    public String getShortDescription(String disease) {
        return diseaseRepository.findByName(disease)
                .orElseThrow(() -> new NotFoundException("Disease not found"))
                .getDescriptionShort();
    }
}
