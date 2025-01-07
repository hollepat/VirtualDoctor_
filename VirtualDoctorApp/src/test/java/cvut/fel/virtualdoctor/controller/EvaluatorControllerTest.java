package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.DiagnosisDTO;
import cvut.fel.virtualdoctor.dto.PatientInputDTO;
import cvut.fel.virtualdoctor.dto.mapper.DiagnosisMapper;
import cvut.fel.virtualdoctor.model.EmergencyType;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.model.PatientInput;
import cvut.fel.virtualdoctor.model.Symptom;
import cvut.fel.virtualdoctor.service.EvaluatorService;
import cvut.fel.virtualdoctor.service.PatientService;
import cvut.fel.virtualdoctor.service.SymptomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class EvaluatorControllerTest {

    @Mock
    private EvaluatorService evaluatorService;

    @Mock
    private PatientService patientService;

    @Mock
    private SymptomService symptomService;

    @Mock
    private DiagnosisMapper diagnosisMapper;

    @InjectMocks
    private EvaluatorController evaluatorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void evaluateDiagnosis_whenNoSymptomsProvided_shouldReturnBadRequest() throws ExecutionException, InterruptedException {
        // Arrange
        PatientInputDTO inputDTO = new PatientInputDTO(
            "John Doe",
            Collections.emptyList(),  // Empty symptoms list
            150.0
        );

        // Act
        CompletableFuture<ResponseEntity<DiagnosisDTO>> future =
            evaluatorController.evaluateDiagnosis(inputDTO);
        ResponseEntity<DiagnosisDTO> response = future.get();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void evaluateDiagnosis_whenAllSymptomsInvalid_shouldReturnBadRequest() throws ExecutionException, InterruptedException {
        // Arrange
        List<String> invalidSymptoms = Arrays.asList("NonExistentSymptom1", "NonExistentSymptom2");
        PatientInputDTO inputDTO = new PatientInputDTO(
            "John Doe",
            invalidSymptoms,
            150.0
        );

        when(symptomService.findByName(anyString())).thenReturn(null);

        // Act
        CompletableFuture<ResponseEntity<DiagnosisDTO>> future = 
            evaluatorController.evaluateDiagnosis(inputDTO);
        ResponseEntity<DiagnosisDTO> response = future.get();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void evaluateDiagnosis_whenMixedValidAndInvalidSymptoms_shouldReturnBadRequest() throws ExecutionException, InterruptedException {
        // Arrange
        List<String> mixedSymptoms = Arrays.asList("ValidSymptom", "InvalidSymptom");
        PatientInputDTO inputDTO = new PatientInputDTO(
            "John Doe",
            mixedSymptoms,
            150.0
        );

        when(symptomService.findByName("ValidSymptom"))
            .thenReturn(new Symptom("ValidSymptom", EmergencyType.NORMAL, "Description"));
        when(symptomService.findByName("InvalidSymptom"))
            .thenReturn(null);

        // Act
        CompletableFuture<ResponseEntity<DiagnosisDTO>> future = 
            evaluatorController.evaluateDiagnosis(inputDTO);
        ResponseEntity<DiagnosisDTO> response = future.get();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void evaluateDiagnosis_whenEvaluatorServiceThrowsException_shouldReturnInternalServerError() throws ExecutionException, InterruptedException {
        // Arrange
        PatientInputDTO inputDTO = new PatientInputDTO(
                "John Doe",
                Arrays.asList("Headache", "Fever"),
                150.0
        );

        Patient patient = new Patient();
        patient.setName("John Doe");
        when(patientService.findByName("John Doe")).thenReturn(patient);

        when(symptomService.findByName("Headache"))
                .thenReturn(new Symptom("Headache", EmergencyType.NORMAL, "Description"));
        when(symptomService.findByName("Fever"))
                .thenReturn(new Symptom("Fever", EmergencyType.NORMAL, "Description"));

        when(evaluatorService.evaluatePatientInput(any(PatientInput.class)))
                .thenReturn(CompletableFuture.failedFuture(
                        new RuntimeException("Evaluation service failed")
                ));

        // Act
        CompletableFuture<ResponseEntity<DiagnosisDTO>> future =
                evaluatorController.evaluateDiagnosis(inputDTO);
        ResponseEntity<DiagnosisDTO> response = future.get();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void evaluateDiagnosis_whenPatientNotFound_shouldReturnInternalServerError() throws ExecutionException, InterruptedException {
        // Arrange
        PatientInputDTO inputDTO = new PatientInputDTO(
                "Unknown Patient",
                Arrays.asList("Headache", "Fever"),
                150.0
        );

        when(symptomService.findByName("Headache"))
                .thenReturn(new Symptom("Headache", EmergencyType.NORMAL, "Description"));
        when(symptomService.findByName("Fever"))
                .thenReturn(new Symptom("Fever", EmergencyType.NORMAL, "Description"));

        when(patientService.findByName("Unknown Patient")).thenReturn(null);

        // Act
        CompletableFuture<ResponseEntity<DiagnosisDTO>> future =
                evaluatorController.evaluateDiagnosis(inputDTO);
        ResponseEntity<DiagnosisDTO> response = future.get();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}