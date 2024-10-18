package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.TraineeController;
import org.example.dto.TrainerDto;
import org.example.dto.requests.trainee.*;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.responses.trainee.*;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.GlobalExceptionHandler;
import org.example.models.TrainingTypeEntity;
import org.example.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TraineeControllerTest {

    @InjectMocks
    private TraineeController traineeController;

    @Mock
    private TraineeService traineeService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerTrainee_Success() throws Exception {
        CreateTraineeRequestDto requestDto = new CreateTraineeRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");

        CreateTraineeResponseDto responseDto = new CreateTraineeResponseDto();
        responseDto.setUsername("johndoe");
        responseDto.setPassword("securePassword");

        when(traineeService.createTrainee(any(CreateTraineeRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(responseDto.getUsername()))
                .andExpect(jsonPath("$.password").value(responseDto.getPassword()));
    }

    @Test
    void updateTrainee_Success() throws Exception {
        UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();
        requestDto.setUsername("johndoe");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setActive(true);

        UpdateTraineeResponseDto responseDto = new UpdateTraineeResponseDto();
        responseDto.setUsername("johndoe");
        responseDto.setFirstName("John");
        responseDto.setLastName("Doe");
        responseDto.setActive(true);

        when(traineeService.updateTrainee(any(UpdateTraineeRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/trainee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(responseDto.getUsername()))
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()));
    }

    @Test
    void updateTrainee_NotFound() throws Exception {
        UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();
        requestDto.setUsername("nonexistentuser");

        when(traineeService.updateTrainee(any(UpdateTraineeRequestDto.class))).thenThrow(
                new EntityNotFoundException());

        mockMvc.perform(put("/trainee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Entity not found"));
    }

    @Test
    void deleteTrainee_Success() throws Exception {
        DeleteTraineeRequestDto requestDto = new DeleteTraineeRequestDto();
        requestDto.setUsername("johndoe");

        mockMvc.perform(delete("/trainee/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    void getNotAssignedTrainers_Success() throws Exception {
        GetNotAssignedTrainersRequestDto requestDto = new GetNotAssignedTrainersRequestDto();
        requestDto.setUsername("testUser");

        TrainerDto trainerDto1 = new TrainerDto();

        trainerDto1.setUsername("Trainer One");
        trainerDto1.setSpecialization(new TrainingTypeEntity("Specialization A"));

        TrainerDto trainerDto2 = new TrainerDto();
        trainerDto2.setUsername("Trainer Two");
        trainerDto2.setSpecialization(new TrainingTypeEntity("Specialization B"));

        List<TrainerDto> trainerDtos = List.of(trainerDto1, trainerDto2);

        GetNotAssignedTrainersResponseDto responseDto = new GetNotAssignedTrainersResponseDto();
        responseDto.setTrainerDtos(trainerDtos);

        when(traineeService.getTrainersNotAssignedToTrainee(any(GetNotAssignedTrainersRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(get("/trainee/notAssignedTrainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.trainerDtos[0].username").value("Trainer One"))
                .andExpect(jsonPath("$.trainerDtos[1].username").value("Trainer Two"));
    }


    @Test
    void getTraineeByUsername_Success() throws Exception {
        GetTraineeByUsernameRequestDto requestDto = new GetTraineeByUsernameRequestDto();
        requestDto.setUsername("johndoe");

        GetTraineeByUsernameResponseDto responseDto = new GetTraineeByUsernameResponseDto();
        responseDto.setFirstName("john");
        responseDto.setLastName("doe");

        when(traineeService.getTraineeByUsername(any(GetTraineeByUsernameRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(get("/trainee/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()));
    }

    @Test
    void getTraineeByUsername_NotFound() throws Exception {
        GetTraineeByUsernameRequestDto requestDto = new GetTraineeByUsernameRequestDto();
        requestDto.setUsername("nonexistentuser");

        when(traineeService.getTraineeByUsername(any(GetTraineeByUsernameRequestDto.class)))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/trainee/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Entity not found"));
    }

    @Test
    void getTrainings_Success() throws Exception {
        GetTraineeTrainingListRequestDto requestDto = new GetTraineeTrainingListRequestDto();
        GetTraineeTrainingListResponseDto responseDto = new GetTraineeTrainingListResponseDto();

        when(traineeService.getTrainingByCriteria(any(GetTraineeTrainingListRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(get("/trainee/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void activateTrainee_Success() throws Exception {
        ActivateUserRequestDto requestDto = new ActivateUserRequestDto();
        requestDto.setUsername("johndoe");

        mockMvc.perform(patch("/trainee/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deactivateTrainee_Success() throws Exception {
        DeactivateUserRequestDto requestDto = new DeactivateUserRequestDto();
        requestDto.setUsername("johndoe");

        mockMvc.perform(patch("/trainee/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }
}
