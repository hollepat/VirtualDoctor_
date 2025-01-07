package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.classifier.client.ClassifierOutputDTO;
import cvut.fel.virtualdoctor.config.SymptomConfig;
import cvut.fel.virtualdoctor.model.*;
import cvut.fel.virtualdoctor.repository.DiagnosisRepository;
import cvut.fel.virtualdoctor.repository.DifferentialListRepository;
import cvut.fel.virtualdoctor.repository.DiseaseRepository;
import cvut.fel.virtualdoctor.repository.PatientDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DiagnosisServiceTest {

    @Mock
    private DiseaseRepository diseaseRepository;

    @Mock
    private DiagnosisRepository diagnosisRepository;

    @Mock
    private DifferentialListRepository differentialListRepository;

    @Mock
    private PatientDataRepository patientDataRepository;

    @Mock
    private SymptomConfig symptomConfig;

    @InjectMocks
    private DiagnosisService diagnosisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void createDiagnosis() {
        // given
        ClassifierInput classifierInput = mock(ClassifierInput.class);
        PatientInput patientInput = mock(PatientInput.class);
        ClassifierOutputDTO classifierOutputDTO = mock(ClassifierOutputDTO.class);
        Patient patient = mock(Patient.class);
        List<Symptom> inputSymptoms = new ArrayList<>(List.of(
                new Symptom("symptom1", EmergencyType.STAY_AT_HOME, "description"),
                new Symptom("symptom2", EmergencyType.NORMAL, "description")
        ));

        Disease disease1 = createDisease("disease1", DoctorType.GENERAL_PRACTITIONER);
        Disease disease2 = createDisease("disease2", DoctorType.ALLERGY_AND_IMMUNOLOGY);
        Disease disease3 = createDisease("disease3", DoctorType.CARDIOLOGY);


        when(diseaseRepository.findByName("disease1")).thenReturn(Optional.of(disease1));
        when(diseaseRepository.findByName("disease2")).thenReturn(Optional.of(disease2));
        when(diseaseRepository.findByName("disease3")).thenReturn(Optional.of(disease3));

        HashMap<String, Double> predictions = new HashMap<>(
                Map.of("disease1", 0.5, "disease2", 0.3, "disease3", 0.2)
        );
        DifferentialList differentialList = new DifferentialList(predictions);

        when(classifierOutputDTO.predictions()).thenReturn(predictions);
        when(classifierOutputDTO.version()).thenReturn("1.0");

        when(differentialListRepository.save(any())).thenReturn(differentialList);

        when(classifierInput.getPatientInput()).thenReturn(patientInput);
        when(patientInput.getPatient()).thenReturn(patient);
        when(patient.getName()).thenReturn("John Doe");
        when(patientInput.getSymptoms()).thenReturn(inputSymptoms);


        // when
        Diagnosis diagnosis = diagnosisService.createDiagnosis(classifierInput, patientInput, classifierOutputDTO);

        // then
        assertThat(diagnosis).isNotNull();
        assertThat(diagnosis.getDoctorsToVisit()).contains(DoctorType.GENERAL_PRACTITIONER, DoctorType.ALLERGY_AND_IMMUNOLOGY, DoctorType.CARDIOLOGY);
        assertThat(diagnosis.getEmergency()).isEqualTo(EmergencyType.STAY_AT_HOME);
        verify(diagnosisRepository).save(diagnosis);
        verify(diseaseRepository, times(3)).findByName(any());
    }

    @Test
    public void createDiagnosis_patientInputWithNoSymptoms_defaultEmergencyLevel() {
        // given
        ClassifierInput classifierInput = mock(ClassifierInput.class);
        PatientInput patientInput = mock(PatientInput.class);
        ClassifierOutputDTO classifierOutputDTO = mock(ClassifierOutputDTO.class);
        Patient patient = mock(Patient.class);
        List<Symptom> inputSymptoms = new ArrayList<>();

        Disease disease1 = createDisease("disease1", DoctorType.GENERAL_PRACTITIONER);
        Disease disease2 = createDisease("disease2", DoctorType.ALLERGY_AND_IMMUNOLOGY);
        Disease disease3 = createDisease("disease3", DoctorType.CARDIOLOGY);


        when(diseaseRepository.findByName("disease1")).thenReturn(Optional.of(disease1));
        when(diseaseRepository.findByName("disease2")).thenReturn(Optional.of(disease2));
        when(diseaseRepository.findByName("disease3")).thenReturn(Optional.of(disease3));

        HashMap<String, Double> predictions = new HashMap<>(
                Map.of("disease1", 0.5, "disease2", 0.3, "disease3", 0.2)
        );
        DifferentialList differentialList = new DifferentialList(predictions);

        when(classifierOutputDTO.predictions()).thenReturn(predictions);
        when(classifierOutputDTO.version()).thenReturn("1.0");

        when(differentialListRepository.save(any())).thenReturn(differentialList);

        when(classifierInput.getPatientInput()).thenReturn(patientInput);
        when(patientInput.getPatient()).thenReturn(patient);
        when(patient.getName()).thenReturn("John Doe");
        when(patientInput.getSymptoms()).thenReturn(inputSymptoms);


        // when
        Diagnosis diagnosis = diagnosisService.createDiagnosis(classifierInput, patientInput, classifierOutputDTO);

        // then
        assertThat(diagnosis).isNotNull();
        assertThat(diagnosis.getEmergency()).isEqualTo(EmergencyType.NORMAL);
        verify(diagnosisRepository).save(diagnosis);
        verify(diseaseRepository, times(3)).findByName(any());
    }

    @Test
    public void createDiagnosis_classifierOutputDTOWithNoDiseases_noDoctorRecommended() {
        // given
        ClassifierInput classifierInput = mock(ClassifierInput.class);
        PatientInput patientInput = mock(PatientInput.class);
        ClassifierOutputDTO classifierOutputDTO = mock(ClassifierOutputDTO.class);
        Patient patient = mock(Patient.class);
        List<Symptom> inputSymptoms = new ArrayList<>(List.of(
                new Symptom("symptom1", EmergencyType.STAY_AT_HOME, "description"),
                new Symptom("symptom2", EmergencyType.NORMAL, "description")
        ));

        HashMap<String, Double> predictions = new HashMap<>();
        DifferentialList differentialList = new DifferentialList(predictions);

        when(classifierOutputDTO.predictions()).thenReturn(predictions);
        when(classifierOutputDTO.version()).thenReturn("1.0");

        when(differentialListRepository.save(any())).thenReturn(differentialList);

        when(classifierInput.getPatientInput()).thenReturn(patientInput);
        when(patientInput.getPatient()).thenReturn(patient);
        when(patient.getName()).thenReturn("John Doe");
        when(patientInput.getSymptoms()).thenReturn(inputSymptoms);


        // when
        Diagnosis diagnosis = diagnosisService.createDiagnosis(classifierInput, patientInput, classifierOutputDTO);

        // then
        assertThat(diagnosis).isNotNull();
        assertThat(diagnosis.getDoctorsToVisit()).isEmpty();
        assertThat(diagnosis.getEmergency()).isEqualTo(EmergencyType.STAY_AT_HOME);
        verify(diagnosisRepository).save(diagnosis);
        verifyNoInteractions(diseaseRepository);
    }

    @Test
    public void markDiagnosis() {
        // given
        UUID id = UUID.randomUUID();
        Diagnosis diagnosis = mock(Diagnosis.class);
        PatientInput patientInput = mock(PatientInput.class);

        String healthData = "{" +
                "\"Temperature\": 36.5," +
                "\"Blood Pressure\": 120.0," +
                "\"BMI\": 22.0" +
                "}";

        String symptoms = "[\"Cough\",\"Fever\"]";

        ClassifierInput classifierInput = new ClassifierInput(
                20,
                Lifestyle.ACTIVE,
                Gender.MALE,
                Location.EUROPE,
                symptoms,
                5.0,
                healthData,
                patientInput
        );
        when(diagnosisRepository.findById(id)).thenReturn(Optional.of(diagnosis));
        when(diagnosis.getClassifierInput()).thenReturn(classifierInput);
        when(symptomConfig.getAvailableSymptoms()).thenReturn(List.of("Cough", "Fever"));

        // when
        diagnosisService.markDiagnosis(id, "disease");

        // then
        verify(patientDataRepository).save(any());
    }

    @Test
    public void markDiagnosis_missingHealthDataKey_RuntimeException() {
        // given
        UUID id = UUID.randomUUID();
        Diagnosis diagnosis = mock(Diagnosis.class);
        PatientInput patientInput = mock(PatientInput.class);

        String healthData = "{" +
                "\"Temperature\": 36.5," +
                "\"Blood Pressure\": 120.0," +
                "}";

        String symptoms = "[\"Cough\",\"Fever\"]";

        ClassifierInput classifierInput = new ClassifierInput(
                20,
                Lifestyle.ACTIVE,
                Gender.MALE,
                Location.EUROPE,
                symptoms,
                5.0,
                healthData,
                patientInput
        );
        when(diagnosisRepository.findById(id)).thenReturn(Optional.of(diagnosis));
        when(diagnosis.getClassifierInput()).thenReturn(classifierInput);
        when(symptomConfig.getAvailableSymptoms()).thenReturn(List.of("Cough", "Fever"));

        // when
        assertThrows(RuntimeException.class, () -> diagnosisService.markDiagnosis(id, "disease"));
    }

    private Disease createDisease(String name, DoctorType doctorType) {
        return new Disease(name, "description", "description", new ArrayList<>(), doctorType);
    }
}
