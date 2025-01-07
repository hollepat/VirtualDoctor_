package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.classifier.client.ClassifierClientRest;
import cvut.fel.virtualdoctor.classifier.client.ClassifierOutputDTO;
import cvut.fel.virtualdoctor.exception.MissingHealthData;
import cvut.fel.virtualdoctor.model.ClassifierInput;
import cvut.fel.virtualdoctor.model.Diagnosis;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.HealthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EvaluatorServiceTest {

    @InjectMocks
    private EvaluatorService evaluatorService;

    @Mock
    private SymptomService symptomService;

    @Mock
    private DiagnosisService diagnosisService;

    @Mock
    private PatientInputService patientInputService;

    @Mock
    private ClassifierInputService classifierInputService;

    @Mock
    private ClassifierClientRest classifierClientRest;

    @Mock
    private HealthDataObserverService healthDataObserverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEvaluatePatientInput() {
        // Arrange
        PatientInput patientInput = new PatientInput();
        HealthData healthData = new HealthData();
        ClassifierInput classifierInput = new ClassifierInput();
        ClassifierOutputDTO classifierOutput = new ClassifierOutputDTO(
                "1.0.0",
                new HashMap<>(Map.of("disease1", 0.8, "disease2", 0.2))
        );
        Diagnosis diagnosis = new Diagnosis();

        when(patientInputService.save(any(PatientInput.class))).thenReturn(patientInput);
        when(healthDataObserverService.provideHealthData(any())).thenReturn(healthData);
        when(classifierInputService.createClassifierInput(any(PatientInput.class), any(HealthData.class))).thenReturn(classifierInput);
        when(classifierClientRest.getPrediction(any(ClassifierInput.class))).thenReturn(CompletableFuture.completedFuture(classifierOutput));
        when(diagnosisService.createDiagnosis(any(ClassifierInput.class), any(PatientInput.class), any(ClassifierOutputDTO.class))).thenReturn(diagnosis);

        // Act
        CompletableFuture<Diagnosis> futureDiagnosis = evaluatorService.evaluatePatientInput(patientInput);

        // Assert
        assertThat(futureDiagnosis).isNotNull();
        Diagnosis result = futureDiagnosis.join(); // Wait for the async task to complete
        assertThat(result).isNotNull();

        // Verify
        verify(patientInputService, times(1)).save(any(PatientInput.class));
        verify(healthDataObserverService, times(1)).provideHealthData(any());
        verify(classifierInputService, times(1)).createClassifierInput(any(PatientInput.class), any(HealthData.class));
        verify(classifierClientRest, times(1)).getPrediction(any(ClassifierInput.class));
        verify(diagnosisService, times(1)).createDiagnosis(any(ClassifierInput.class), any(PatientInput.class), any(ClassifierOutputDTO.class));
    }

    @Test
    void testEvaluatePatientInput_MissingHealthDataForPatient_throwsMissingHealthDataException() {
        // Arrange
        PatientInput patientInput = new PatientInput();
        when(patientInputService.save(any(PatientInput.class))).thenReturn(patientInput);
        when(healthDataObserverService.provideHealthData(any())).thenThrow(new MissingHealthData("Health data not available"));

        // Act & Assert
        MissingHealthData exception = assertThrows(MissingHealthData.class, () -> evaluatorService.evaluatePatientInput(patientInput).join());

        assertThat(exception.getMessage()).isEqualTo("Health data not available");

        // Verify interactions
        verify(patientInputService, times(1)).save(any(PatientInput.class));
        verify(healthDataObserverService, times(1)).provideHealthData(any());
        verifyNoInteractions(classifierInputService, classifierClientRest, diagnosisService);
    }

    @Test
    void testEvaluatePatientInput_getPredicationFailed_diagnosisNotCreated() {
        // Arrange
        PatientInput patientInput = new PatientInput();
        HealthData healthData = new HealthData();
        ClassifierInput classifierInput = new ClassifierInput();

        when(patientInputService.save(any(PatientInput.class))).thenReturn(patientInput);
        when(healthDataObserverService.provideHealthData(any())).thenReturn(healthData);
        when(classifierInputService.createClassifierInput(any(PatientInput.class), any(HealthData.class))).thenReturn(classifierInput);

        // Simulate a failed future from classifier client
        Exception e = new RuntimeException("Classifier service failed");
        when(classifierClientRest.getPrediction(any(ClassifierInput.class)))
                .thenReturn(CompletableFuture.failedFuture(e));

        // Act
        CompletableFuture<Diagnosis> futureDiagnosis = evaluatorService.evaluatePatientInput(patientInput);

        // Assert
        assertThrows(CompletionException.class, futureDiagnosis::join);

        // Verify interactions
        verify(patientInputService, times(1)).save(any(PatientInput.class));
        verify(healthDataObserverService, times(1)).provideHealthData(any());
        verify(classifierInputService, times(1)).createClassifierInput(any(PatientInput.class), any(HealthData.class));
        verify(classifierClientRest, times(1)).getPrediction(any(ClassifierInput.class));
        verifyNoInteractions(diagnosisService);
    }


    @Test
    public void testEvaluatePatientInput_createDiagnosisFails() {
        // Arrange
        PatientInput patientInput = new PatientInput();
        HealthData healthData = new HealthData();
        ClassifierInput classifierInput = new ClassifierInput();
        ClassifierOutputDTO classifierOutput = new ClassifierOutputDTO(
                "1.0.0",
                new HashMap<>(Map.of("disease1", 0.8, "disease2", 0.2))
        );

        when(patientInputService.save(patientInput)).thenReturn(patientInput);
        when(healthDataObserverService.provideHealthData(any())).thenReturn(healthData);
        when(classifierInputService.createClassifierInput(patientInput, healthData)).thenReturn(classifierInput);
        when(classifierClientRest.getPrediction(any(ClassifierInput.class))).thenReturn(CompletableFuture.completedFuture(classifierOutput));

        when(diagnosisService.createDiagnosis(any(), any(), any())).thenThrow(new RuntimeException("Failed to create diagnosis"));

        // Act
        CompletableFuture<Diagnosis> result = evaluatorService.evaluatePatientInput(patientInput);

        // Assert
        assertThrows(Exception.class, result::join, "Expected exception but none was thrown");
    }
}