package services;

import org.example.dao.TraineeDao;
import org.example.dto.TrainerDto;
import org.example.dto.requests.trainee.*;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.responses.trainee.*;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingMapper;
import org.example.models.Trainee;
import org.example.models.Training;
import org.example.service.impl.TraineeServiceImpl;
import org.example.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainee_validRequest_shouldCreateTrainee() {
        CreateTraineeRequestDto requestDto = new CreateTraineeRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");

        Trainee trainee = new Trainee();
        when(traineeMapper.toTrainee(requestDto)).thenReturn(trainee);
        when(userService.getAllExistingUsernames()).thenReturn(List.of("existingUsername"));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        CreateTraineeResponseDto responseDto = new CreateTraineeResponseDto();
        when(traineeMapper.toCreateTraineeDto(any(Trainee.class))).thenReturn(responseDto);

        CreateTraineeResponseDto result = traineeService.createTrainee(requestDto);

        assertThat(result).isNotNull();
        verify(traineeDao, times(1)).create(any(Trainee.class));
    }

    @Test
    void updateTrainee_validRequest_shouldUpdateTrainee() {
        UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();
        requestDto.setUsername("john_doe");
        Trainee trainee = new Trainee();

        when(traineeDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(trainee));
        when(traineeMapper.toTrainee(requestDto)).thenReturn(trainee);
        when(userService.isValid(trainee)).thenReturn(true);

        List<TrainerDto> trainerDtos = new ArrayList<>();
        when(trainerMapper.convertTrainersToDto(anyList())).thenReturn(trainerDtos);

        UpdateTraineeResponseDto responseDto = new UpdateTraineeResponseDto();
        when(traineeMapper.toUpdateTraineeDto(trainee, trainerDtos)).thenReturn(responseDto);

        UpdateTraineeResponseDto result = traineeService.updateTrainee(requestDto);

        assertThat(result).isNotNull();
        verify(traineeDao, times(1)).update(any(Trainee.class));
    }

    @Test
    void deleteTrainee_validRequest_shouldDeleteTrainee() {
        DeleteTraineeRequestDto requestDto = new DeleteTraineeRequestDto();
        requestDto.setUsername("john_doe");

        when(traineeDao.delete("john_doe")).thenReturn(true);

        traineeService.deleteTrainee(requestDto);

        verify(traineeDao, times(1)).delete("john_doe");
    }

    @Test
    void getTraineeByUsername_validRequest_shouldReturnTrainee() {
        GetTraineeByUsernameRequestDto requestDto = new GetTraineeByUsernameRequestDto();
        requestDto.setUsername("john_doe");

        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("john_doe")).thenReturn(Optional.of(trainee));

        List<TrainerDto> trainers = new ArrayList<>();
        when(trainerMapper.convertTrainersToDto(anyList())).thenReturn(trainers);

        GetTraineeByUsernameResponseDto responseDto = new GetTraineeByUsernameResponseDto();
        when(traineeMapper.toGetTraineeByUsernameDto(trainee, trainers)).thenReturn(responseDto);

        GetTraineeByUsernameResponseDto result = traineeService.getTraineeByUsername(requestDto);

        assertThat(result).isNotNull();
        verify(traineeDao, times(1)).findByUsername("john_doe");
    }

    @Test
    void changePassword_validRequest_shouldChangePassword() {
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setUsername("john_doe");
        requestDto.setPassword("new_password");

        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("john_doe")).thenReturn(Optional.of(trainee));

        traineeService.changePassword(requestDto);

        verify(passwordEncoder, times(1)).encode("new_password");
        verify(traineeDao, times(1)).update(trainee);
    }

    @Test
    void activateTrainee_validRequest_shouldActivateTrainee() {
        ActivateUserRequestDto requestDto = new ActivateUserRequestDto();
        requestDto.setUsername("john_doe");

        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("john_doe")).thenReturn(Optional.of(trainee));

        traineeService.activateTrainee(requestDto);

        verify(userService, times(1)).activate(trainee);
    }

    @Test
    void deactivateTrainee_validRequest_shouldDeactivateTrainee() {
        DeactivateUserRequestDto requestDto = new DeactivateUserRequestDto();
        requestDto.setUsername("john_doe");

        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("john_doe")).thenReturn(Optional.of(trainee));

        traineeService.deactivateTrainee(requestDto);

        verify(userService, times(1)).deactivate(trainee);
    }

    @Test
    void getTrainingByCriteria_validRequest_shouldReturnTrainings() {
        GetTraineeTrainingListRequestDto requestDto = new GetTraineeTrainingListRequestDto();
        requestDto.setUsername("john_doe");

        List<Training> trainings = new ArrayList<>();
        when(traineeDao.getTrainingByCriteria(anyString(), any(), any(), anyString(), any()))
                .thenReturn(trainings);

        GetTraineeTrainingListResponseDto responseDto = new GetTraineeTrainingListResponseDto();
        when(traineeMapper.toGetTraineeTrainingListDto(anyList())).thenReturn(responseDto);

        GetTraineeTrainingListResponseDto result = traineeService.getTrainingByCriteria(requestDto);

        assertThat(result).isNotNull();
    }
}
