package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.HealthDataDTO;
import cvut.fel.virtualdoctor.exception.NotFoundException;
import cvut.fel.virtualdoctor.model.HealthData;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.service.HealthDataObserverService;
import cvut.fel.virtualdoctor.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HealthDataObserverControllerTest {

    @Mock
    private HealthDataObserverService healthDataObserverService;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private HealthDataObserverController healthDataObserverController;

    private final HealthDataDTO validHealthDataDTO = new HealthDataDTO(
            36.6,
            120,
            60,
            LocalDateTime.parse("2021-06-01T12:00:00"),
            "John Doe"
    );

    private final Patient samplePatient = new Patient();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        samplePatient.setName("John Doe");
        samplePatient.setHeight(180);
        samplePatient.setWeight(75);
    }

    @Test
    void saveHealthData_withValidData_shouldReturnSuccess() {
        // Arrange
        when(patientService.findByName("John Doe")).thenReturn(samplePatient);
        doNothing().when(healthDataObserverService).update(any(HealthData.class));

        // Act
        ResponseEntity<String> response = healthDataObserverController.saveHealthData(validHealthDataDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data received", response.getBody());

        // Verify
        verify(patientService).findByName("John Doe");
        verify(healthDataObserverService).update(any(HealthData.class));
    }

    @Test
    void saveHealthData_withUnknownPatient_shouldHandleError() {
        // Arrange
        when(patientService.findByName("John Doe")).thenThrow(new NotFoundException("Patient not found"));

        // Act & Assert
        assertThrows(NotFoundException.class, () ->
                healthDataObserverController.saveHealthData(validHealthDataDTO)
        );

        // Verify
        verify(healthDataObserverService, never()).update(any(HealthData.class));
    }
}