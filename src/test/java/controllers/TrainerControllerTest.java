package controllers;

import org.example.controller.TrainerController;
import org.example.dto.requests.trainer.*;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.requests.user.LoginRequestDto;
import org.example.dto.responses.trainer.*;
import org.example.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TrainerControllerTest {

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerTrainer_shouldReturnCreatedResponse() {
        CreateTrainerRequestDto requestDto = new CreateTrainerRequestDto();
        CreateTrainerResponseDto responseDto = new CreateTrainerResponseDto();

        when(trainerService.createTrainer(requestDto)).thenReturn(responseDto);

        ResponseEntity<CreateTrainerResponseDto> response = trainerController.registerTrainer(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    void registerTrainer_shouldReturnInternalServerError() {
        CreateTrainerRequestDto requestDto = new CreateTrainerRequestDto();

        when(trainerService.createTrainer(requestDto)).thenThrow(new RuntimeException());

        ResponseEntity<CreateTrainerResponseDto> response = trainerController.registerTrainer(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void login_shouldReturnOkResponse() {
        LoginRequestDto requestDto = new LoginRequestDto();

        doNothing().when(trainerService).getTrainerByUsernameAndPassword(requestDto);

        ResponseEntity<Void> response = trainerController.login(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void login_shouldReturnUnauthorizedResponse() {
        LoginRequestDto requestDto = new LoginRequestDto();

        doThrow(new RuntimeException()).when(trainerService).getTrainerByUsernameAndPassword(requestDto);

        ResponseEntity<Void> response = trainerController.login(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getTrainerByUsername_shouldReturnOkResponse() {
        GetTrainerByUsernameRequestDto requestDto = new GetTrainerByUsernameRequestDto();
        GetTrainerByUsernameResponseDto responseDto = new GetTrainerByUsernameResponseDto();

        when(trainerService.getTrainerByUsername(requestDto)).thenReturn(responseDto);

        ResponseEntity<GetTrainerByUsernameResponseDto> response = trainerController.getTrainerByUsername(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    void getTrainerByUsername_shouldReturnNotFoundResponse() {
        GetTrainerByUsernameRequestDto requestDto = new GetTrainerByUsernameRequestDto();

        when(trainerService.getTrainerByUsername(requestDto)).thenReturn(null);

        ResponseEntity<GetTrainerByUsernameResponseDto> response = trainerController.getTrainerByUsername(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getTrainings_shouldReturnOkResponse() {
        GetTrainerTrainingListRequestDto requestDto = new GetTrainerTrainingListRequestDto();
        GetTrainerTrainingListResponseDto responseDto = new GetTrainerTrainingListResponseDto();

        when(trainerService.getTrainingByCriteria(requestDto)).thenReturn(responseDto);

        ResponseEntity<GetTrainerTrainingListResponseDto> response = trainerController.getTrainings(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    void updateTrainer_shouldReturnOkResponse() {
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();
        UpdateTrainerResponseDto responseDto = new UpdateTrainerResponseDto();

        when(trainerService.updateTrainer(requestDto)).thenReturn(responseDto);

        ResponseEntity<UpdateTrainerResponseDto> response = trainerController.updateTrainer(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    void updateTrainer_shouldReturnNotFoundResponse() {
        UpdateTrainerRequestDto requestDto = new UpdateTrainerRequestDto();

        when(trainerService.updateTrainer(requestDto)).thenReturn(null);

        ResponseEntity<UpdateTrainerResponseDto> response = trainerController.updateTrainer(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void activateTrainer_shouldReturnOkResponse() {
        ActivateUserRequestDto requestDto = new ActivateUserRequestDto();

        ResponseEntity<Void> response = trainerController.activateTrainer(requestDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deactivateTrainer_shouldReturnOkResponse() {
        DeactivateUserRequestDto requestDto = new DeactivateUserRequestDto();

        ResponseEntity<Void> response = trainerController.deactivateTrainer(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void changePassword_shouldReturnOkResponse() {
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();

        ResponseEntity<Void> response = trainerController.changePassword(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
