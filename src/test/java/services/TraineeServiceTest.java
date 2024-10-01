package services;

import org.example.dao.TraineeDao;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.dto.requests.trainee.*;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.responses.trainee.CreateTraineeResponseDto;
import org.example.dto.responses.trainee.GetNotAssignedTrainersResponseDto;
import org.example.dto.responses.trainee.GetTraineeByUsernameResponseDto;
import org.example.dto.responses.trainee.GetTraineeTrainingListResponseDto;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingMapper;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.example.service.impl.TraineeServiceImpl;
import org.example.service.impl.UserService;
import org.example.util.Generator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserService userService;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainee_ValidRequest() {
        CreateTraineeRequestDto requestDto = new CreateTraineeRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");

        when(userService.getAllExistingUsernames()).thenReturn(List.of("existing.username"));
        when(traineeMapper.toTrainee(requestDto)).thenReturn(trainee);

        try (MockedStatic<Generator> generatorMock = mockStatic(Generator.class)) {
            generatorMock.when(() -> Generator.generateUsername(anyString(), anyString(), anyList()))
                    .thenReturn("john.doe");
            generatorMock.when(Generator::generatePassword).thenReturn("password123");

            CreateTraineeResponseDto responseDto = new CreateTraineeResponseDto();
            responseDto.setUsername("john.doe");
            responseDto.setPassword("password123");

            when(traineeMapper.toCreateTraineeDto(trainee)).thenReturn(responseDto);

            CreateTraineeResponseDto result = traineeService.createTrainee(requestDto);

            assertThat(result).isNotNull();
            assertThat(result.getUsername()).isEqualTo("john.doe");
            assertThat(result.getPassword()).isEqualTo("password123");

            verify(traineeDao).create(any(Trainee.class));
        }
    }

    @Test
    void updateTrainee_ValidRequest() {
        UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();
        requestDto.setUsername("john.doe");

        Trainee trainee = new Trainee();
        trainee.setUsername("john.doe");

        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        when(traineeMapper.toTrainee(requestDto)).thenReturn(trainee);

        List<Trainer> trainers = List.of(new Trainer());
        when(traineeDao.getTrainersAssignedToTrainee("john.doe")).thenReturn(trainers);

        List<TrainerDto> trainerDtos = List.of(new TrainerDto());
        when(trainerMapper.convertTrainersToDto(trainers)).thenReturn(trainerDtos);

        traineeService.updateTrainee(requestDto);

        verify(traineeDao).update(trainee);
        verify(trainerMapper).convertTrainersToDto(trainers);
    }

    @Test
    void deleteTrainee_ValidRequest() {
        DeleteTraineeRequestDto requestDto = new DeleteTraineeRequestDto();
        requestDto.setUsername("john.doe");

        when(traineeDao.delete("john.doe")).thenReturn(true);

        traineeService.deleteTrainee(requestDto);

        verify(traineeDao).delete("john.doe");
    }

    @Test
    void getTraineeByUsername_ValidRequest() {
        GetTraineeByUsernameRequestDto requestDto = new GetTraineeByUsernameRequestDto();
        requestDto.setUsername("john.doe");

        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        List<Trainer> trainers = List.of(new Trainer());
        when(traineeDao.getTrainersAssignedToTrainee("john.doe")).thenReturn(trainers);

        List<TrainerDto> trainerDtos = List.of(new TrainerDto());
        when(trainerMapper.convertTrainersToDto(trainers)).thenReturn(trainerDtos);

        GetTraineeByUsernameResponseDto responseDto = new GetTraineeByUsernameResponseDto();
        when(traineeMapper.toGetTraineeByUsernameDto(trainee, trainerDtos)).thenReturn(responseDto);

        GetTraineeByUsernameResponseDto result = traineeService.getTraineeByUsername(requestDto);

        assertThat(result).isNotNull();
        verify(traineeDao).findByUsername("john.doe");
        verify(trainerMapper).convertTrainersToDto(trainers);
    }

    @Test
    void changePassword_ValidRequest() {
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setUsername("john.doe");
        requestDto.setOldPassword("oldPass");
        requestDto.setPassword("newPass");

        when(userService.isAuthenticated("john.doe", "oldPass")).thenReturn(true);

        Trainee trainee = new Trainee();
        trainee.setUsername("john.doe");

        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        traineeService.changePassword(requestDto);

        verify(traineeDao).update(trainee);
        assertThat(trainee.getPassword()).isEqualTo("newPass");
    }

    @Test
    void activateTrainee_ValidRequest() {
        ActivateUserRequestDto requestDto = new ActivateUserRequestDto();
        requestDto.setUsername("john.doe");

        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));

        traineeService.activateTrainee(requestDto);

        verify(userService).activate(trainee);
    }

    @Test
    void deactivateTrainee_ValidRequest() {
        DeactivateUserRequestDto requestDto = new DeactivateUserRequestDto();
        requestDto.setUsername("john.doe");

        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(trainee));
        traineeService.deactivateTrainee(requestDto);

        verify(userService).deactivate(trainee);
    }

    @Test
    void getTrainingByCriteria_ValidRequest() {
        GetTraineeTrainingListRequestDto requestDto = new GetTraineeTrainingListRequestDto();
        requestDto.setUsername("john.doe");
        requestDto.setFromDate(new Date());
        requestDto.setToDate(new Date());
        requestDto.setTrainerName("Trainer Name");
        requestDto.setTrainingType(new TrainingTypeEntity("Strength"));

        List<Training> trainings = List.of(new Training());
        when(traineeDao.getTrainingByCriteria(
                requestDto.getUsername(),
                requestDto.getFromDate(),
                requestDto.getToDate(),
                requestDto.getTrainerName(),
                requestDto.getTrainingType()
        )).thenReturn(trainings);

        List<TrainingDto> trainingDtos = List.of(new TrainingDto());
        when(trainingMapper.convertTrainingsToDto(trainings)).thenReturn(trainingDtos);

        GetTraineeTrainingListResponseDto responseDto = new GetTraineeTrainingListResponseDto();
        when(traineeMapper.toGetTraineeTrainingListDto(trainingDtos)).thenReturn(responseDto);

        GetTraineeTrainingListResponseDto result = traineeService.getTrainingByCriteria(requestDto);

        assertThat(result).isNotNull();
        verify(traineeDao).getTrainingByCriteria(
                requestDto.getUsername(),
                requestDto.getFromDate(),
                requestDto.getToDate(),
                requestDto.getTrainerName(),
                requestDto.getTrainingType()
        );
    }

    @Test
    void getTrainersNotAssignedToTrainee_ValidRequest() {
        GetNotAssignedTrainersRequestDto requestDto = new GetNotAssignedTrainersRequestDto();
        requestDto.setUsername("john.doe");

        List<Trainer> trainers = List.of(new Trainer());
        when(traineeDao.getTrainersNotAssignedToTrainee(requestDto.getUsername())).thenReturn(trainers);

        List<TrainerDto> trainerDtos = List.of(new TrainerDto());
        when(trainerMapper.convertTrainersToDto(trainers)).thenReturn(trainerDtos);

        GetNotAssignedTrainersResponseDto responseDto = new GetNotAssignedTrainersResponseDto();
        when(traineeMapper.toGetNotAssignedTrainersDto(trainerDtos)).thenReturn(responseDto);

        GetNotAssignedTrainersResponseDto result = traineeService.getTrainersNotAssignedToTrainee(requestDto);

        assertThat(result).isNotNull();

        verify(traineeDao).getTrainersNotAssignedToTrainee(requestDto.getUsername());
    }
}

