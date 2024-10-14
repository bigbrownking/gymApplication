package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.TrainerController;
import org.example.dto.requests.trainee.CreateTraineeRequestDto;
import org.example.dto.requests.trainee.GetTraineeByUsernameRequestDto;
import org.example.dto.requests.trainee.UpdateTraineeRequestDto;
import org.example.dto.requests.trainer.*;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.responses.trainer.*;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.GlobalExceptionHandler;
import org.example.models.TrainingTypeEntity;
import org.example.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TrainerControllerTest {

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainerService trainerService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerTrainer_Success() throws Exception {
        CreateTrainerRequestDto requestDto = new CreateTrainerRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setSpecialization(new TrainingTypeEntity("Strength"));

        CreateTrainerResponseDto responseDto = new CreateTrainerResponseDto();
        responseDto.setUsername("johndoe");
        responseDto.setPassword("securepassword");

        when(trainerService.createTrainer(any(CreateTrainerRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/trainer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(responseDto.getUsername()))
                .andExpect(jsonPath("$.password").value(responseDto.getPassword()));
    }
    @Test
    void updateTrainer_Success() throws Exception {
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
        requestDto.setUsername("johndoe");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setSpecialization(new TrainingTypeEntity("Yoga"));
        requestDto.setActive(true);

        UpdateTrainerResponseDto responseDto = new UpdateTrainerResponseDto();
        responseDto.setUsername("johndoe");
        responseDto.setFirstName("John");
        responseDto.setLastName("Doe");
        responseDto.setSpecialization(new TrainingTypeEntity("Bench Press"));
        responseDto.setActive(true);

        when(trainerService.updateTrainer(any(UpdateTrainerRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/trainer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(responseDto.getUsername()))
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()));
    }

    @Test
    void updateTrainer_NotFound() throws Exception {
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
        requestDto.setUsername("johndoe");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setSpecialization(new TrainingTypeEntity("Yoga"));
        requestDto.setActive(true);

        when(trainerService.updateTrainer(any(UpdateTrainerRequestDto.class)))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(put("/trainer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Entity not found"));
    }

    @Test
    void getTrainerByUsername_Success() throws Exception {
        GetTrainerByUsernameRequestDto requestDto = new GetTrainerByUsernameRequestDto();
        requestDto.setUsername("johndoe");

        GetTrainerByUsernameResponseDto responseDto = new GetTrainerByUsernameResponseDto();
        responseDto.setFirstname("John");
        responseDto.setLastname("Doe");
        responseDto.setSpecialization(new TrainingTypeEntity("Yoga"));
        responseDto.setActive(true);
        responseDto.setTraineeDtos(new ArrayList<>());

        when(trainerService.getTrainerByUsername(any(GetTrainerByUsernameRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(get("/trainer/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname").value(responseDto.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(responseDto.getLastname()));
    }

    @Test
    void getTrainerByUsername_NotFound() throws Exception {
        GetTrainerByUsernameRequestDto requestDto = new GetTrainerByUsernameRequestDto();
        requestDto.setUsername("nonexistentuser");

        when(trainerService.getTrainerByUsername(any(GetTrainerByUsernameRequestDto.class)))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/trainer/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Entity not found"));
    }

    @Test
    void activateTrainer_Success() throws Exception {
        ActivateUserRequestDto requestDto = new ActivateUserRequestDto();
        requestDto.setUsername("johndoe");

        mockMvc.perform(patch("/trainer/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deactivateTrainer_Success() throws Exception {
        DeactivateUserRequestDto requestDto = new DeactivateUserRequestDto();
        requestDto.setUsername("johndoe");

        mockMvc.perform(patch("/trainer/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }
}
