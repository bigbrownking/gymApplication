package controllers;

import org.example.controller.TrainingController;
import org.example.dto.requests.training.CreateTrainingRequestDto;
import org.example.dto.responses.training.GetTrainingTypesResponseDto;
import org.example.models.TrainingTypeEntity;
import org.example.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    }

    @Test
    void testGetTrainingTypes_Success() throws Exception {
        GetTrainingTypesResponseDto responseDto = new GetTrainingTypesResponseDto();
        List<TrainingTypeEntity> trainingTypes = new ArrayList<>();
        trainingTypes.add(new TrainingTypeEntity("Yoga"));
        trainingTypes.add(new TrainingTypeEntity("Strength Training"));
        responseDto.setTrainingTypeEntities(trainingTypes);

        when(trainingService.getTrainingTypes()).thenReturn(responseDto);

        mockMvc.perform(get("/training/types")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"trainingTypeEntities\":[{\"trainingTypeName\":\"Yoga\"},{\"trainingTypeName\":\"Strength Training\"}]}"));

        verify(trainingService).getTrainingTypes();
    }


    @Test
    void testAddTraining_Success() throws Exception {
        CreateTrainingRequestDto createTrainingRequestDto = new CreateTrainingRequestDto();
        createTrainingRequestDto.setTraineeUsername("traineeUser");
        createTrainingRequestDto.setTrainerUsername("trainerUser");
        createTrainingRequestDto.setTrainingName("Strength Training");
        createTrainingRequestDto.setTrainingDate(new Date());
        createTrainingRequestDto.setDuration(60);

        doNothing().when(trainingService).createTraining(any(CreateTrainingRequestDto.class));

        mockMvc.perform(post("/training/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"traineeUsername\":\"traineeUser\",\"trainerUsername\":\"trainerUser\",\"trainingName\":\"Strength Training\",\"trainingDate\":\"2024-10-16T10:00:00.000+00:00\",\"duration\":60}"))
                .andExpect(status().isCreated());

        verify(trainingService).createTraining(any(CreateTrainingRequestDto.class));
    }
}
