import org.example.dao.TraineeDao;
import org.example.models.Trainee;
import org.example.service.impl.TraineeServiceImpl;
import org.example.util.Generator;
import org.example.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.anyList;


@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    public void setup(){
        traineeService.setUserService(userService);
    }


    @Test
    public void shouldCreateTraineeSuccessfully() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        List<String> existingUsernames = new ArrayList<>();
        when(userService.getAllExistingUsernames()).thenReturn(existingUsernames);

        try (MockedStatic<Generator> generatorMockedStatic = mockStatic(Generator.class)) {
            generatorMockedStatic.when(() -> Generator.generateUsername("John", "Doe", existingUsernames))
                    .thenReturn("John.Doe");
            generatorMockedStatic.when(Generator::generatePassword)
                    .thenReturn("password123");
            generatorMockedStatic.when(() -> Generator.generateId(anyList()))
                    .thenReturn(1L);


            traineeService.createTrainee(trainee);

            verify(userService, times(1)).getAllExistingUsernames();
            verify(traineeDao, times(1)).create(trainee);
            assertThat(trainee.getUsername()).isEqualTo("John.Doe");
            assertThat(trainee.getPassword()).isEqualTo("password123");
            assertThat(trainee.getUserId()).isEqualTo(1L);
        }
    }

    @Test
    public void shouldUpdateTraineeSuccessfully() {
        Trainee trainee = new Trainee();
        traineeService.updateTrainee(trainee);
        verify(traineeDao, times(1)).update(trainee);
    }

    @Test
    public void givenUsername_whenGetTraineeByUsername_thenSuccess() {
        Trainee trainee = new Trainee();
        String username = "John.Doe";
        when(traineeDao.findByUsername(username)).thenReturn(Optional.of(trainee));

        Trainee result = traineeService.getTraineeByUsername(username);

        verify(traineeDao, times(1)).findByUsername(username);
        assertThat(result).isEqualTo(trainee);
    }

    @Test
    public void getAllTraineeSuccessfully() {
        List<Trainee> trainees = new ArrayList<>();
        when(traineeDao.listAll()).thenReturn(trainees);

        List<Trainee> result = traineeService.getAllTrainee();

        verify(traineeDao, times(1)).listAll();
        assertThat(result).isEqualTo(trainees);
    }
}
