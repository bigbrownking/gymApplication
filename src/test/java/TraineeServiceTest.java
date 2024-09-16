import org.example.dao.TraineeDao;
import org.example.models.Trainee;
import org.example.service.impl.TraineeServiceImpl;
import org.example.service.impl.UserService;
import org.example.util.Generator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testCreateTrainee_ValidTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        try (MockedStatic<Generator> generatorMock = mockStatic(Generator.class)) {
            when(userService.isValid(trainee)).thenReturn(true);
            when(userService.getAllExistingUsernames()).thenReturn(Arrays.asList("existing.username"));

            generatorMock.when(() -> Generator.generateUsername(anyString(), anyString(), anyList()))
                    .thenReturn("john.doe");
            generatorMock.when(Generator::generatePassword)
                    .thenReturn("password123");

            traineeService.createTrainee(trainee);

            assertThat(trainee.getUsername()).isEqualTo("john.doe");
            assertThat(trainee.getPassword()).isEqualTo("password123");
            assertThat(trainee.isActive()).isTrue();
            verify(traineeDao).create(trainee);
        }
    }



    @Test
    void testCreateTrainee_InvalidTrainee() {
        Trainee trainee = new Trainee();
        when(userService.isValid(trainee)).thenReturn(false);

        traineeService.createTrainee(trainee);

        // Verify no interaction with the traineeDao when invalid trainee
        verify(traineeDao, never()).create(trainee);
    }

    @Test
    void testCreateTrainee_InvalidTrainee_ThrowsException() {
        Trainee trainee = new Trainee();
        when(userService.isValid(trainee)).thenReturn(false);

        verify(traineeDao, never()).create(trainee);
    }

    @Test
    void testUpdateTrainee_ValidTrainee_Authenticated() {
        Trainee trainee = new Trainee();
        trainee.setUsername("john.doe");
        trainee.setPassword("password123");

        when(userService.isAuthenticated(anyString(), anyString())).thenReturn(true);
        when(userService.isValid(any(Trainee.class))).thenReturn(true);

        traineeService.updateTrainee(trainee);

        // Verify that the trainee was updated
        verify(traineeDao).update(trainee);
    }

    @Test
    void testUpdateTrainee_InvalidTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUsername("john.doe");
        trainee.setPassword("password123");

        when(userService.isAuthenticated(anyString(), anyString())).thenReturn(true);
        when(userService.isValid(any(Trainee.class))).thenReturn(false);

        traineeService.updateTrainee(trainee);

        // Verify that no update occurred due to invalid trainee
        verify(traineeDao, never()).update(trainee);
    }

    @Test
    void testDeleteTrainee_TraineeFound() {
        when(traineeDao.delete("john.doe")).thenReturn(true);

        traineeService.deleteTrainee("john.doe");

        // Verify that the trainee was deleted
        verify(traineeDao).delete("john.doe");
    }

    @Test
    void testDeleteTrainee_TraineeNotFound() {
        when(traineeDao.delete("john.doe")).thenReturn(false);

        traineeService.deleteTrainee("john.doe");

        // Since trainee wasn't found, no further actions are taken
        verify(traineeDao).delete("john.doe");
    }

    @Test
    void testGetTraineeByUsername_TraineeFound() {
        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        Trainee result = traineeService.getTraineeByUsername("john.doe");

        // Verify that the correct trainee is returned
        assertThat(result).isEqualTo(trainee);
    }

    @Test
    void testGetTraineeByUsername_TraineeNotFound() {
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.empty());

        Trainee result = traineeService.getTraineeByUsername("john.doe");

        // Verify that null is returned if trainee is not found
        assertThat(result).isNull();
    }

    @Test
    void testGetTraineeByUsernameAndPassword_Authenticated() {
        Trainee trainee = new Trainee();
        when(userService.isAuthenticated("john.doe", "password123")).thenReturn(true);
        when(traineeDao.findByUsernameAndPassword("john.doe", "password123")).thenReturn(Optional.of(trainee));

        Trainee result = traineeService.getTraineeByUsernameAndPassword("john.doe", "password123");

        // Verify that the correct trainee is returned when authenticated
        assertThat(result).isEqualTo(trainee);
    }

    @Test
    void testGetTraineeByUsernameAndPassword_AuthenticationFailed() {
        when(userService.isAuthenticated("john.doe", "password123")).thenReturn(false);

        Trainee result = traineeService.getTraineeByUsernameAndPassword("john.doe", "password123");

        // Verify that null is returned if authentication fails
        assertThat(result).isNull();
    }

    @Test
    void testActivateTrainee_TraineeFound() {
        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        traineeService.activateTrainee("john.doe");

        // Verify that the trainee was activated
        verify(userService).activate(trainee);
    }

    @Test
    void testActivateTrainee_TraineeNotFound() {
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.empty());

        traineeService.activateTrainee("john.doe");

        // Verify that activation doesn't happen when trainee is not found
        verify(userService, never()).activate(any());
    }

    @Test
    void testDeactivateTrainee_TraineeFound() {
        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        traineeService.deactivateTrainee("john.doe");

        // Verify that the trainee was deactivated
        verify(userService).deactivate(trainee);
    }

    @Test
    void testDeactivateTrainee_TraineeNotFound() {
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.empty());

        traineeService.deactivateTrainee("john.doe");

        // Verify that deactivation doesn't happen when trainee is not found
        verify(userService, never()).deactivate(any());
    }
}
