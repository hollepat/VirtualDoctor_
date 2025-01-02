package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.exception.MissingHealthData;
import cvut.fel.virtualdoctor.exception.NotFoundException;
import cvut.fel.virtualdoctor.model.HealthData;
import cvut.fel.virtualdoctor.model.Patient;
import cvut.fel.virtualdoctor.repository.HealthDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HealthDataObserverServiceTest {

    @Mock
    private HealthDataRepository healthDataRepository;

    @InjectMocks
    private HealthDataObserverService healthDataObserverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProvideHealthData_happy_path() {
        // Arrange
        Patient patient = new Patient();
        patient.setName("John Doe");

        List<HealthData> healthDataList = List.of(
                new HealthData(patient, LocalDateTime.now(), 36.5, 120.0, 22.0, 80),
                new HealthData(patient, LocalDateTime.now(), 37.0, 125.0, 22.0, 85),
                new HealthData(patient, LocalDateTime.now(), 36.0, 118.0, 22.0, 78)
        );

        when(healthDataRepository.findByPatientName(patient.getName())).thenReturn(healthDataList);

        // Act
        HealthData result = healthDataObserverService.provideHealthData(patient);

        // Assert
        assertThat(result.getSkinTemperature()).isEqualTo(36.5);
        assertThat(result.getBloodPressure()).isEqualTo(121.0);
        assertThat(result.getBmi()).isEqualTo(22.0);
        assertThat(result.getHeartRate()).isEqualTo(81);
        verify(healthDataRepository).findByPatientName(patient.getName());

    }

    @Test
    void testProvideHealthData_noData() {
        // Arrange
        Patient patient = new Patient();
        patient.setName("John Doe");

        when(healthDataRepository.findByPatientName(patient.getName())).thenReturn(List.of());

        // Act & Assert
        assertThrows(MissingHealthData.class, () -> healthDataObserverService.provideHealthData(patient));

        verify(healthDataRepository).findByPatientName(patient.getName());
    }
}
