package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.SymptomDTO;
import cvut.fel.virtualdoctor.model.Symptom;
import cvut.fel.virtualdoctor.repository.SymptomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SymptomService implements ISymptomService {

    private final SymptomRepository symptomRepository;

    public List<Symptom> getAllSymptoms() {
        return symptomRepository.findAll();
    }

    public void addSymptom(Symptom symptom) {
        symptomRepository.findAll().forEach(s -> {
            if (s.getName().equals(symptom.getName())) {
                throw new IllegalArgumentException("Symptom with name " + symptom.getName() + " already exists");
            }
        });
        symptomRepository.save(symptom);
    }

    public void deleteSymptom(SymptomDTO symptomDTO) {
        Symptom symptomToDelete = symptomRepository.findByName(symptomDTO.name()).orElseThrow(
                () -> new IllegalArgumentException("Symptom with name " + symptomDTO.name() + " does not exist")
        );
        symptomRepository.delete(symptomToDelete);
    }
}
