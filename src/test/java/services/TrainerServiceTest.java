package services;

import org.example.dao.TrainerDao;
import org.example.dto.TraineeDto;
import org.example.dto.TrainingDto;
import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.GetTrainerByUsernameRequestDto;
import org.example.dto.requests.trainer.GetTrainerTrainingListRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.responses.trainer.CreateTrainerResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerTrainingListResponseDto;
import org.example.dto.responses.trainer.UpdateTrainerResponseDto;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingMapper;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.service.impl.TrainerServiceImpl;
import org.example.service.impl.UserService;
import org.example.util.Generator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserService userService;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer_ValidRequest() {
        CreateTrainerRequestDto requestDto = new CreateTrainerRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");

        Trainer trainer = new Trainer();
        when(trainerMapper.toTrainer(requestDto)).thenReturn(trainer);
        when(userService.getAllExistingUsernames()).thenReturn(List.of("existingUser"));

        try (MockedStatic<Generator> mockedGenerator = mockStatic(Generator.class)) {
            mockedGenerator.when(() -> Generator.generateUsername(trainer.getFirstName(), trainer.getLastName(), List.of("existingUser")))
                    .thenReturn("john.doe");
            mockedGenerator.when(Generator::generatePassword)
                    .thenReturn("password");

            CreateTrainerResponseDto expectedResponse = new CreateTrainerResponseDto();
            when(trainerMapper.toCreateTrainerDto(trainer)).thenReturn(expectedResponse);

            CreateTrainerResponseDto response = trainerService.createTrainer(requestDto);

            assertThat(response).isEqualTo(expectedResponse);
            verify(trainerDao).create(trainer);
        }
    }
    @Test
    void updateTrainer_ValidRequest() {
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
        requestDto.setUsername("john.doe");

        Trainer trainer = new Trainer();
        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(trainer));
        when(trainerMapper.toTrainer(requestDto)).thenReturn(trainer);

        List<TraineeDto> traineeDtos = List.of(new TraineeDto());
        when(traineeMapper.convertTraineesToDto(trainerDao.allTraineesOfTrainer(trainer.getUsername())))
                .thenReturn(traineeDtos);

        UpdateTrainerResponseDto expectedResponse = new UpdateTrainerResponseDto();
        when(trainerMapper.toUpdateTrainerDto(trainer, traineeDtos)).thenReturn(expectedResponse);

        UpdateTrainerResponseDto response = trainerService.updateTrainer(requestDto);

        assertThat(response).isEqualTo(expectedResponse);
        verify(trainerDao).update(trainer);
    }

    @Test
    void getTrainerByUsername_ValidRequest() {
        GetTrainerByUsernameRequestDto requestDto = new GetTrainerByUsernameRequestDto();
        requestDto.setUsername("john.doe");

        Trainer trainer = new Trainer();
        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(trainer));

        List<TraineeDto> traineeDtos = List.of(new TraineeDto());
        when(traineeMapper.convertTraineesToDto(trainerDao.allTraineesOfTrainer(requestDto.getUsername())))
                .thenReturn(traineeDtos);

        GetTrainerByUsernameResponseDto expectedResponse = new GetTrainerByUsernameResponseDto();
        when(trainerMapper.toGetTrainerByUsernameDto(trainer, traineeDtos)).thenReturn(expectedResponse);

        GetTrainerByUsernameResponseDto response = trainerService.getTrainerByUsername(requestDto);

        assertThat(response).isEqualTo(expectedResponse);
        verify(trainerDao).findByUsername(requestDto.getUsername());
    }

    @Test
    void changePassword_ValidRequest() {
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setUsername("john.doe");
        requestDto.setOldPassword("oldPassword");
        requestDto.setPassword("newPassword");

        Trainer trainer = new Trainer();
        when(userService.isAuthenticated(requestDto.getUsername(), requestDto.getOldPassword())).thenReturn(true);
        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(trainer));

        trainerService.changePassword(requestDto);

        assertThat(trainer.getPassword()).isEqualTo("newPassword");
        verify(trainerDao).update(trainer);
    }

    @Test
    void activateTrainer_ValidRequest() {
        ActivateUserRequestDto requestDto = new ActivateUserRequestDto();
        requestDto.setUsername("john.doe");

        Trainer trainer = new Trainer();
        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(trainer));

        trainerService.activateTrainer(requestDto);

        verify(userService).activate(trainer);
    }

    @Test
    void deactivateTrainer_ValidRequest() {
        DeactivateUserRequestDto requestDto = new DeactivateUserRequestDto();
        requestDto.setUsername("john.doe");

        Trainer trainer = new Trainer();
        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(trainer));

        trainerService.deactivateTrainer(requestDto);

        verify(userService).deactivate(trainer);
    }

    @Test
    void getTrainingByCriteria_ValidRequest() {
        GetTrainerTrainingListRequestDto requestDto = new GetTrainerTrainingListRequestDto();
        requestDto.setUsername("john.doe");
        requestDto.setFromDate(new Date());
        requestDto.setToDate(new Date());
        requestDto.setTraineeName("Trainee Name");

        List<Training> trainings = List.of(new Training());
        when(trainerDao.getTrainingByCriteria(
                requestDto.getUsername(),
                requestDto.getFromDate(),
                requestDto.getToDate(),
                requestDto.getTraineeName()
        )).thenReturn(trainings);

        List<TrainingDto> trainingDtos = List.of(new TrainingDto());
        when(trainingMapper.convertTrainingsToDto(trainings)).thenReturn(trainingDtos);

        GetTrainerTrainingListResponseDto expectedResponse = new GetTrainerTrainingListResponseDto();
        when(trainerMapper.toGetTrainingListDto(trainingDtos)).thenReturn(expectedResponse);

        GetTrainerTrainingListResponseDto response = trainerService.getTrainingByCriteria(requestDto);

        assertThat(response).isEqualTo(expectedResponse);
        verify(trainerDao).getTrainingByCriteria(
                requestDto.getUsername(),
                requestDto.getFromDate(),
                requestDto.getToDate(),
                requestDto.getTraineeName()
        );
    }
}
