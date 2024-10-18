package services;

import org.example.dao.TrainerDao;
import org.example.dao.TrainingDao;

import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.GetTrainerByUsernameRequestDto;
import org.example.dto.requests.trainer.GetTrainerTrainingListRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.responses.trainer.CreateTrainerResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerTrainingListResponseDto;
import org.example.dto.responses.trainer.UpdateTrainerResponseDto;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingMapper;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.example.service.impl.TrainerServiceImpl;
import org.example.service.impl.UserService;
import org.example.util.Generator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

class TrainerServiceTest {

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private UserService userService;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer_ShouldCreateTrainerSuccessfully() {
        CreateTrainerRequestDto requestDto = new CreateTrainerRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setSpecialization(new TrainingTypeEntity("Yoga"));

        List<String> existingUsernames = new ArrayList<>();
        existingUsernames.add("johndoe");

        Trainer trainer = new Trainer();
        trainer.setFirstName(requestDto.getFirstName());
        trainer.setLastName(requestDto.getLastName());
        trainer.setSpecialization(requestDto.getSpecialization());

        when(userService.getAllExistingUsernames()).thenReturn(existingUsernames);
        when(trainerMapper.toTrainer(requestDto)).thenReturn(trainer);
        when(trainingDao.getTrainingTypes()).thenReturn(List.of(
                new TrainingTypeEntity("Yoga"), new TrainingTypeEntity("Pilates")));
        when(trainerMapper.toCreateTrainerDto(any())).thenReturn(new CreateTrainerResponseDto());

        CreateTrainerResponseDto response = trainerService.createTrainer(requestDto);

        assertThat(response).isNotNull();
        verify(trainerDao).create(any(Trainer.class));
    }

    @Test
    void createTrainer_ShouldReturnNull_WhenRequestIsNull() {
        CreateTrainerResponseDto response = trainerService.createTrainer(null);

        assertThat(response).isNull();
    }

    @Test
    void updateTrainer_ShouldUpdateTrainerSuccessfully() {
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
        requestDto.setUsername("johndoe");

        Trainer trainer = new Trainer();
        trainer.setUsername("johndoe");

        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(trainer));
        when(trainerMapper.toTrainer(requestDto)).thenReturn(trainer);
        when(userService.isValid(any())).thenReturn(true);
        when(traineeMapper.convertTraineesToDto(any())).thenReturn(new ArrayList<>());
        when(trainerMapper.toUpdateTrainerDto(any(), any())).thenReturn(new UpdateTrainerResponseDto());

        UpdateTrainerResponseDto response = trainerService.updateTrainer(requestDto);

        assertThat(response).isNotNull();
        verify(trainerDao).update(trainer);
    }

    @Test
    void updateTrainer_ShouldReturnNull_WhenTrainerNotFound() {
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
        requestDto.setUsername("nonexistent");

        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());

        UpdateTrainerResponseDto response = trainerService.updateTrainer(requestDto);

        assertThat(response).isNull();
    }

    @Test
    void getTrainerByUsername_ShouldReturnTrainerSuccessfully() {
        GetTrainerByUsernameRequestDto requestDto = new GetTrainerByUsernameRequestDto();
        requestDto.setUsername("johndoe");

        Trainer trainer = new Trainer();
        trainer.setUsername("johndoe");

        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(trainer));
        when(traineeMapper.convertTraineesToDto(any())).thenReturn(new ArrayList<>());
        when(trainerMapper.toGetTrainerByUsernameDto(any(), any())).thenReturn(new GetTrainerByUsernameResponseDto());

        GetTrainerByUsernameResponseDto response = trainerService.getTrainerByUsername(requestDto);

        assertThat(response).isNotNull();
        verify(trainerDao).findByUsername(requestDto.getUsername());
    }

    @Test
    void getTrainerByUsername_ShouldReturnNull_WhenTrainerNotFound() {
        GetTrainerByUsernameRequestDto requestDto = new GetTrainerByUsernameRequestDto();
        requestDto.setUsername("nonexistent");

        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());

        GetTrainerByUsernameResponseDto response = trainerService.getTrainerByUsername(requestDto);

        assertThat(response).isNull();
    }

    @Test
    void changePassword_ShouldChangePasswordSuccessfully() {
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setUsername("johndoe");
        requestDto.setPassword("newPassword");

        Trainer trainer = new Trainer();
        trainer.setUsername("johndoe");

        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(trainer));

        trainerService.changePassword(requestDto);

        verify(passwordEncoder).encode(requestDto.getPassword());
        verify(trainerDao).update(trainer);
    }

    @Test
    void changePassword_ShouldDoNothing_WhenTrainerNotFound() {
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        requestDto.setUsername("nonexistent");

        when(trainerDao.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());
        trainerService.changePassword(requestDto);

        verify(trainerDao, never()).update(any());
    }

    @Test
    void getTrainingByCriteria_ShouldReturnTrainingListSuccessfully() {
        GetTrainerTrainingListRequestDto requestDto = new GetTrainerTrainingListRequestDto();
        requestDto.setUsername("johndoe");
        requestDto.setFromDate(null);
        requestDto.setToDate(null);
        requestDto.setTraineeName(null);

        Training training = new Training();
        List<Training> trainingList = List.of(training);

        when(trainerDao.getTrainingByCriteria(any(), any(), any(), any())).thenReturn(trainingList);
        when(trainingMapper.convertTrainingsToDto(any())).thenReturn(new ArrayList<>());
        when(trainerMapper.toGetTrainingListDto(any())).thenReturn(new GetTrainerTrainingListResponseDto());

        GetTrainerTrainingListResponseDto response = trainerService.getTrainingByCriteria(requestDto);

        assertThat(response).isNotNull();
    }

    @Test
    void getPasswordFromTrainer_ShouldReturnPasswordSuccessfully() {
        String username = "johndoe";
        Trainer trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setPassword("hashedPassword");

        when(trainerDao.findByUsername(username)).thenReturn(Optional.of(trainer));
        String password = trainerService.getPasswordFromTrainer(username);

        assertThat(password).isEqualTo("hashedPassword");
    }

    @Test
    void getPasswordFromTrainer_ShouldReturnNull_WhenTrainerNotFound() {
        String username = "nonexistent";

        when(trainerDao.findByUsername(username)).thenReturn(Optional.empty());
        String password = trainerService.getPasswordFromTrainer(username);

        assertThat(password).isNull();
    }
}
