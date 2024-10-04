package controllers;

import org.example.controller.TrainingController;
import org.example.dto.requests.training.CreateTrainingRequestDto;
import org.example.dto.responses.training.GetTrainingTypesResponseDto;
import org.example.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TrainingControllerTest {

    @InjectMocks
    private TrainingController trainingController;

    @Mock
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTrainingTypes_shouldReturnTrainingTypes() throws Exception {
        GetTrainingTypesResponseDto expectedResponse = new GetTrainingTypesResponseDto();
        when(trainingService.getTrainingTypes()).thenReturn(expectedResponse);

        ResponseEntity<GetTrainingTypesResponseDto> responseEntity = trainingController.getTrainingTypes();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse);
        verify(trainingService, times(1)).getTrainingTypes();
    }

    @Test
    void getTrainingTypes_shouldReturnInternalServerError_onException() throws Exception {
        when(trainingService.getTrainingTypes()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<GetTrainingTypesResponseDto> responseEntity = trainingController.getTrainingTypes();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(trainingService, times(1)).getTrainingTypes();
    }

    @Test
    void addTraining_shouldReturnCreatedStatus() throws Exception {
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto();
        doNothing().when(trainingService).createTraining(requestDto);

        ResponseEntity<Void> responseEntity = trainingController.addTraining(requestDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(trainingService, times(1)).createTraining(requestDto);
    }

    @Test
    void addTraining_shouldReturnInternalServerError_onException() throws Exception {
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto();
        doThrow(new RuntimeException("Database error")).when(trainingService).createTraining(requestDto);

        ResponseEntity<Void> responseEntity = trainingController.addTraining(requestDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(trainingService, times(1)).createTraining(requestDto);
    }
}
