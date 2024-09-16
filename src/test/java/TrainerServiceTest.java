import org.example.dao.TrainerDao;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.example.service.impl.TrainerServiceImpl;
import org.example.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
public class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer_ShouldCreateTrainer_WhenValid() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setSpecialization(new TrainingTypeEntity("Specialization"));
        when(userService.isValid(any(Trainer.class))).thenReturn(true);
        when(userService.getAllExistingUsernames()).thenReturn(List.of());
        when(trainerDao.create(any(Trainer.class))).thenReturn(true);

        // Act
        trainerService.createTrainer(trainer);

        // Assert
        assertThat(trainer.getUsername()).isNotNull();
        assertThat(trainer.getPassword()).isNotNull();
        verify(trainerDao).create(trainer);
    }

    @Test
    void createTrainer_ShouldNotCreateTrainer_WhenSpecializationDoesNotExist() {
        Trainer trainer = new Trainer();
        trainer.setSpecialization(new TrainingTypeEntity("NonExistingSpecialization"));
        when(userService.isValid(any(Trainer.class))).thenReturn(false);
        when(userService.getAllExistingUsernames()).thenReturn(List.of());
        when(trainerDao.create(any(Trainer.class))).thenReturn(false);

        trainerService.createTrainer(trainer);

        verify(trainerDao, never()).create(trainer);
    }

    @Test
    void updateTrainer_ShouldUpdateTrainer_WhenValid() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setUsername("username");
        trainer.setPassword("password");
        when(userService.isAuthenticated(eq("username"), eq("password"))).thenReturn(true);
        when(userService.isValid(any(Trainer.class))).thenReturn(true);

        // Act
        trainerService.updateTrainer(trainer);

        // Assert
        verify(trainerDao).update(trainer);
    }

    @Test
    void updateTrainer_ShouldNotUpdateTrainer_WhenInvalid() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setUsername("username");
        trainer.setPassword("password");
        when(userService.isAuthenticated(eq("username"), eq("password"))).thenReturn(true);
        when(userService.isValid(any(Trainer.class))).thenReturn(false);

        // Act
        trainerService.updateTrainer(trainer);

        // Assert
        verify(trainerDao, never()).update(trainer);
    }

    @Test
    void updateTrainer_ShouldNotUpdateTrainer_WhenAuthenticationFails() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setUsername("username");
        trainer.setPassword("password");
        when(userService.isAuthenticated(eq("username"), eq("password"))).thenReturn(false);

        // Act
        trainerService.updateTrainer(trainer);

        // Assert
        verify(trainerDao, never()).update(trainer);
    }

    @Test
    void getTrainerByUsername_ShouldReturnTrainer_WhenExists() {
        // Arrange
        Trainer trainer = new Trainer();
        when(trainerDao.findByUsername(eq("username"))).thenReturn(Optional.of(trainer));

        // Act
        Trainer result = trainerService.getTrainerByUsername("username");

        // Assert
        assertThat(result).isEqualTo(trainer);
    }

    @Test
    void getTrainerByUsername_ShouldReturnNull_WhenNotExists() {
        // Arrange
        when(trainerDao.findByUsername(eq("username"))).thenReturn(Optional.empty());

        // Act
        Trainer result = trainerService.getTrainerByUsername("username");

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void getAllTrainers_ShouldReturnListOfTrainers() {
        // Arrange
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());
        when(trainerDao.listAll()).thenReturn(trainers);

        // Act
        List<Trainer> result = trainerService.getAllTrainers();

        // Assert
        assertThat(result).isEqualTo(trainers);
    }

    @Test
    void getTrainerByUsernameAndPassword_ShouldReturnTrainer_WhenAuthenticated() {
        // Arrange
        Trainer trainer = new Trainer();
        when(userService.isAuthenticated(eq("username"), eq("password"))).thenReturn(true);
        when(trainerDao.findByUsernameAndPassword(eq("username"), eq("password"))).thenReturn(Optional.of(trainer));

        // Act
        Trainer result = trainerService.getTrainerByUsernameAndPassword("username", "password");

        // Assert
        assertThat(result).isEqualTo(trainer);
    }

    @Test
    void getTrainerByUsernameAndPassword_ShouldReturnNull_WhenAuthenticationFails() {
        // Arrange
        when(userService.isAuthenticated(eq("username"), eq("password"))).thenReturn(false);

        // Act
        Trainer result = trainerService.getTrainerByUsernameAndPassword("username", "password");

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void changePassword_ShouldChangePassword_WhenAuthenticated() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setUsername("username");
        trainer.setPassword("oldPassword");
        String newPassword = "newPassword";
        when(userService.isAuthenticated(eq("username"), eq("oldPassword"))).thenReturn(true);

        // Act
        trainerService.changePassword(trainer, newPassword);

        // Assert
        assertThat(trainer.getPassword()).isEqualTo(newPassword);
        verify(trainerDao).update(trainer);
    }

    @Test
    void changePassword_ShouldNotChangePassword_WhenAuthenticationFails() {
        // Arrange
        Trainer trainer = new Trainer();
        trainer.setUsername("username");
        trainer.setPassword("oldPassword");
        String newPassword = "newPassword";
        when(userService.isAuthenticated(eq("username"), eq("oldPassword"))).thenReturn(false);

        // Act
        trainerService.changePassword(trainer, newPassword);

        // Assert
        assertThat(trainer.getPassword()).isEqualTo("oldPassword");
        verify(trainerDao, never()).update(trainer);
    }

    @Test
    void activateTrainer_ShouldActivateTrainer_WhenExists() {
        // Arrange
        Trainer trainer = new Trainer();
        when(trainerDao.findByUsername(eq("username"))).thenReturn(Optional.of(trainer));

        // Act
        trainerService.activateTrainer("username");

        // Assert
        verify(userService).activate(trainer);
    }

    @Test
    void activateTrainer_ShouldNotActivateTrainer_WhenNotFound() {
        // Arrange
        when(trainerDao.findByUsername(eq("username"))).thenReturn(Optional.empty());

        // Act
        trainerService.activateTrainer("username");

        // Assert
        verify(userService, never()).activate(any(Trainer.class));
    }

    @Test
    void deactivateTrainer_ShouldDeactivateTrainer_WhenExists() {
        // Arrange
        Trainer trainer = new Trainer();
        when(trainerDao.findByUsername(eq("username"))).thenReturn(Optional.of(trainer));

        // Act
        trainerService.deactivateTrainer("username");

        // Assert
        verify(userService).deactivate(trainer);
    }

    @Test
    void deactivateTrainer_ShouldNotDeactivateTrainer_WhenNotFound() {
        // Arrange
        when(trainerDao.findByUsername(eq("username"))).thenReturn(Optional.empty());

        // Act
        trainerService.deactivateTrainer("username");

        // Assert
        verify(userService, never()).deactivate(any(Trainer.class));
    }

    @Test
    void getTrainingByCriteria_ShouldReturnListOfTrainings() {
        // Arrange
        List<Training> trainings = List.of(new Training(), new Training());
        when(trainerDao.getTrainingByCriteria(eq("username"), any(Date.class), any(Date.class), eq("traineeName")))
                .thenReturn(trainings);

        // Act
        List<Training> result = trainerService.getTrainingByCriteria("username", new Date(), new Date(), "traineeName");

        // Assert
        assertThat(result).isEqualTo(trainings);
    }
}
