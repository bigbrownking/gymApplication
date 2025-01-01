package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.trainee.UpdateTraineeRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.dto.requests.user.GetProfileRequest;
import org.example.dto.responses.trainee.UpdateTraineeResponseDto;
import org.example.dto.responses.trainer.UpdateTrainerResponseDto;
import org.example.models.TrainingTypeEntity;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import steps.shared.RequestSteps;
import util.TraineeDetector;

import java.time.LocalDateTime;

public class UpdateSteps {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    private Object request;
    private Object response;

    public UpdateSteps() {
        traineeService = Mockito.mock(TraineeService.class);
        trainerService = Mockito.mock(TrainerService.class);
    }

    @When("update {string}")
    public void update(String option){
        request = RequestSteps.getRequest();
        System.out.println("Request set to: " + request);
        if (TraineeDetector.isTrainee(option)) {
            if (request instanceof UpdateTraineeRequestDto) {
                response = updateTrainee((UpdateTraineeRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for trainee update");
            }
        } else {
            if (request instanceof UpdateTrainerRequestDto) {
                response = updateTrainer((UpdateTrainerRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for trainer update");
            }
        }
    }
    private UpdateTraineeResponseDto updateTrainee(UpdateTraineeRequestDto updateTraineeRequestDto){
        UpdateTraineeResponseDto mockResponse = new UpdateTraineeResponseDto(
                "some.name", "some", "name", LocalDateTime.now(), "address", true, null);
        Mockito.when(traineeService.updateTrainee(updateTraineeRequestDto)).thenReturn(mockResponse);
        return traineeService.updateTrainee(updateTraineeRequestDto);

    }
    private UpdateTrainerResponseDto updateTrainer(UpdateTrainerRequestDto updateTrainerRequestDto){
        UpdateTrainerResponseDto mockResponse = new UpdateTrainerResponseDto(
                "some.name", "some", "name", new TrainingTypeEntity("Yoga"), true, null);
        Mockito.when(trainerService.updateTrainer(updateTrainerRequestDto)).thenReturn(mockResponse);
        return trainerService.updateTrainer(updateTrainerRequestDto);
    }

    @Then("the response should contain the updated {string} details")
    public void should_update_user(String userType){
        Assert.assertNotNull("Response should not be null", response);

        if (TraineeDetector.isTrainee(userType)) {
            UpdateTraineeResponseDto traineeResponse = (UpdateTraineeResponseDto) response;

            Assert.assertEquals("some.name", traineeResponse.getUsername());
            Assert.assertEquals("some", traineeResponse.getFirstName());
            Assert.assertEquals("name", traineeResponse.getLastName());
            Assert.assertTrue(traineeResponse.isActive());
            Mockito.verify(traineeService).updateTrainee((UpdateTraineeRequestDto) request);

        } else{
            UpdateTrainerResponseDto trainerResponse = (UpdateTrainerResponseDto) response;

            Assert.assertEquals("some.name", trainerResponse.getUsername());
            Assert.assertEquals("some", trainerResponse.getFirstName());
            Assert.assertEquals("name", trainerResponse.getLastName());
            Assert.assertNotNull(trainerResponse.getSpecialization());
            Assert.assertTrue(trainerResponse.isActive());

            Mockito.verify(trainerService).updateTrainer((UpdateTrainerRequestDto) request);
        }
    }
}
