import org.example.dao.TrainerDao;
import org.example.models.Trainer;
import org.example.service.impl.TrainerServiceImpl;
import org.example.util.Generator;
import org.example.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.anyList;


@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    public void setup(){
        trainerService.setUserService(userService);
    }

    @Test
    void shouldCreateTrainerSuccessfully() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");

        List<String> existingUsernames = Collections.singletonList("existingUser");

        when(userService.getAllExistingUsernames()).thenReturn(existingUsernames);

        try (MockedStatic<Generator> generatorMockedStatic = mockStatic(Generator.class)) {
            generatorMockedStatic.when(() -> Generator.generateUsername("John", "Doe", existingUsernames))
                    .thenReturn("John.Doe");
            generatorMockedStatic.when(Generator::generatePassword)
                    .thenReturn("password123");
            generatorMockedStatic.when(() -> Generator.generateId(anyList()))
                    .thenReturn(1L);

            trainerService.createTrainer(trainer);

            assertThat(trainer.getUsername()).isEqualTo("John.Doe");
            assertThat(trainer.getPassword()).isEqualTo("password123");
            assertThat(trainer.getUserId()).isEqualTo(1L);

            verify(userService, times(1)).getAllExistingUsernames();
            verify(trainerDao, times(1)).create(trainer);
        }
    }

    @Test
    void shouldUpdateTrainerSuccessfully() {
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        trainer.setUsername("John.Doe");

        trainerService.updateTrainer(trainer);

        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void givenUsername_whenGetTrainerByUsername_thenSuccess() {
        String username = "John.Doe";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);

        when(trainerDao.findByUsername(username)).thenReturn(Optional.of(trainer));

        Trainer result = trainerService.getTrainer(username);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        verify(trainerDao, times(1)).findByUsername(username);
    }

    @Test
    void givenNonexistentUsername_whenGetTrainerByUsername_thenGetNullTrainer() {
        String username = "nonexistent";

        when(trainerDao.findByUsername(username)).thenReturn(Optional.empty());

        Trainer result = trainerService.getTrainer(username);

        assertThat(result).isNull();
        verify(trainerDao, times(1)).findByUsername(username);
    }

    @Test
    void getAllTrainersSuccessfully() {
        List<Trainer> trainers = Collections.singletonList(new Trainer());
        when(trainerDao.listAll()).thenReturn(trainers);

        List<Trainer> result = trainerService.getAllTrainers();

        assertThat(result.size()).isEqualTo(trainers.size());
        assertThat(result).isEqualTo(trainers);
        verify(trainerDao, times(1)).listAll();
    }
}
