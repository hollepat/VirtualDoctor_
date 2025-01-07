package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.PatientDTO;
import cvut.fel.virtualdoctor.model.Gender;
import cvut.fel.virtualdoctor.model.Lifestyle;
import cvut.fel.virtualdoctor.model.Location;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private final PatientDTO validPatientDTO = new PatientDTO(
            "John Doe",
            30,
            180,
            75,
            Gender.MALE,
            Location.EUROPE,
            Lifestyle.ACTIVE
    );

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_withValidPatient_shouldReturnSuccessMessage() {
        // Arrange
        Patient expectedPatient = new Patient(
                validPatientDTO.name(),
                validPatientDTO.age(),
                validPatientDTO.height(),
                validPatientDTO.weight(),
                validPatientDTO.gender(),
                validPatientDTO.location(),
                validPatientDTO.lifestyle()
        );
        
        when(patientService.createPatient(any(Patient.class))).thenReturn(expectedPatient);

        // Act
        ResponseEntity<String> response = patientController.createUser(validPatientDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
                String.format("Patient %s created successfully!", validPatientDTO.name()),
                response.getBody()
        );
    }

    @Test
    void createUser_withExistingPatient_shouldReturnBadRequest() {
        // Arrange
        when(patientService.createPatient(any(Patient.class))).thenReturn(null);

        // Act
        ResponseEntity<String> response = patientController.createUser(validPatientDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                String.format("Patient %s already exists!", validPatientDTO.name()),
                response.getBody()
        );
    }
}