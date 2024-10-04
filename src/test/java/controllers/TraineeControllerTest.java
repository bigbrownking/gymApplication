package controllers;

import static org.mockito.Mockito.*;

import org.example.controller.TraineeController;
import org.example.dto.requests.trainee.*;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.LoginRequestDto;
import org.example.dto.responses.trainee.*;
import org.example.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TraineeControllerTest {

    private TraineeService traineeService;
    private TraineeController traineeController;

    @BeforeEach
    public void setUp() {
        traineeService = mock(TraineeService.class);
        traineeController = new TraineeController(traineeService);
    }

    @Test
    public void testRegisterTrainee_Success() throws Exception {
        CreateTraineeRequestDto requestDto = new CreateTraineeRequestDto();
        CreateTraineeResponseDto responseDto = new CreateTraineeResponseDto();

        when(traineeService.createTrainee(requestDto)).thenReturn(responseDto);

        ResponseEntity<CreateTraineeResponseDto> response = traineeController.registerTrainee(requestDto);

        verify(traineeService).createTrainee(requestDto);
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() == responseDto;
    }

    @Test
    public void testLogin_Success() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();

        doNothing().when(traineeService).getTraineeByUsernameAndPassword(loginRequestDto);

        ResponseEntity<Void> response = traineeController.login(loginRequestDto);

        verify(traineeService).getTraineeByUsernameAndPassword(loginRequestDto);
        assert response.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void testUpdateTrainee_Success() throws Exception {
        UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();
        UpdateTraineeResponseDto responseDto = new UpdateTraineeResponseDto();

        when(traineeService.updateTrainee(requestDto)).thenReturn(responseDto);

        ResponseEntity<UpdateTraineeResponseDto> response = traineeController.updateTrainee(requestDto);

        verify(traineeService).updateTrainee(requestDto);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == responseDto;
    }

    @Test
    public void testUpdateTrainee_NotFound() throws Exception {
        UpdateTraineeRequestDto requestDto = new UpdateTraineeRequestDto();

        when(traineeService.updateTrainee(requestDto)).thenReturn(null);

        ResponseEntity<UpdateTraineeResponseDto> response = traineeController.updateTrainee(requestDto);

        verify(traineeService).updateTrainee(requestDto);
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void testDeleteTrainee_Success() throws Exception {
        DeleteTraineeRequestDto requestDto = new DeleteTraineeRequestDto();

        doNothing().when(traineeService).deleteTrainee(requestDto);

        ResponseEntity<Void> response = traineeController.deleteTrainee(requestDto);

        verify(traineeService).deleteTrainee(requestDto);
        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    @Test
    public void testGetTraineeByUsername_Success() throws Exception {
        GetTraineeByUsernameRequestDto requestDto = new GetTraineeByUsernameRequestDto();
        GetTraineeByUsernameResponseDto responseDto = new GetTraineeByUsernameResponseDto();

        when(traineeService.getTraineeByUsername(requestDto)).thenReturn(responseDto);

        ResponseEntity<GetTraineeByUsernameResponseDto> response = traineeController.getTraineeByUsername(requestDto);

        verify(traineeService).getTraineeByUsername(requestDto);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == responseDto;
    }

    @Test
    public void testGetTraineeByUsername_NotFound() throws Exception {
        GetTraineeByUsernameRequestDto requestDto = new GetTraineeByUsernameRequestDto();

        when(traineeService.getTraineeByUsername(requestDto)).thenReturn(null);

        ResponseEntity<GetTraineeByUsernameResponseDto> response = traineeController.getTraineeByUsername(requestDto);

        verify(traineeService).getTraineeByUsername(requestDto);
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    public void testChangePassword_Success() throws Exception {
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();

        doNothing().when(traineeService).changePassword(requestDto);

        ResponseEntity<Void> response = traineeController.changePassword(requestDto);

        verify(traineeService).changePassword(requestDto);
        assert response.getStatusCode() == HttpStatus.OK;
    }

}
