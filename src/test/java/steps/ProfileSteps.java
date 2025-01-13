package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.user.GetProfileRequest;
import org.example.dto.responses.trainee.GetTraineeByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.models.TrainingTypeEntity;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import steps.shared.RequestSteps;
import util.TraineeDetector;

import java.time.LocalDateTime;

public class ProfileSteps {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    private Object request;
    private Object response;

    public ProfileSteps() {
        traineeService = Mockito.mock(TraineeService.class);
        trainerService = Mockito.mock(TrainerService.class);
    }

    @When("profile {string}")
    public void profile(String option) {
        request = RequestSteps.getRequest();
        System.out.println("Request set to: " + request);
        if (TraineeDetector.isTrainee(option)) {
            if (request instanceof GetProfileRequest) {
                response = profileTrainee((GetProfileRequest) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for trainee profile");
            }
        } else {
            if (request instanceof GetProfileRequest) {
                response = profileTrainer((GetProfileRequest) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for trainer profile");
            }
        }
    }

    private Object profileTrainee(GetProfileRequest getProfileRequest) {
        // Mocking service response for trainee profile
        GetTraineeByUsernameResponseDto mockResponse = new GetTraineeByUsernameResponseDto("some", "name", LocalDateTime.now(), "address", true, null);
        Mockito.when(traineeService.getTraineeByUsername(getProfileRequest)).thenReturn(mockResponse);
        return traineeService.getTraineeByUsername(getProfileRequest);
    }

    private Object profileTrainer(GetProfileRequest getProfileRequest) {
        // Mocking service response for trainer profile
        GetTrainerByUsernameResponseDto mockResponse = new GetTrainerByUsernameResponseDto("some", "name", new TrainingTypeEntity("Yoga"), true, null);
        Mockito.when(trainerService.getTrainerByUsername(getProfileRequest)).thenReturn(mockResponse);
        return trainerService.getTrainerByUsername(getProfileRequest);
    }

    @Then("the response should contain the {string} profile details")
    public void profile_should_be_retrieved(String userType) {
        Assert.assertNotNull("Response should not be null", response);
        if (TraineeDetector.isTrainee(userType)) {
            GetTraineeByUsernameResponseDto traineeResponse = (GetTraineeByUsernameResponseDto) response;
            Assert.assertEquals("some", traineeResponse.getFirstName());
            Assert.assertEquals("name", traineeResponse.getLastName());
            Assert.assertTrue(traineeResponse.isActive());
            Mockito.verify(traineeService).getTraineeByUsername((GetProfileRequest) request);
        } else {
            GetTrainerByUsernameResponseDto trainerResponse = (GetTrainerByUsernameResponseDto) response;
            Assert.assertEquals("some", trainerResponse.getFirstname());
            Assert.assertEquals("name", trainerResponse.getLastname());
            Assert.assertTrue(trainerResponse.isActive());
            Mockito.verify(trainerService).getTrainerByUsername((GetProfileRequest) request);
        }
    }

    @And("{string} profile doesn't exist")
    public void doesnt_exist(String userType){
        GetProfileRequest getProfileRequest = (GetProfileRequest) request;
        if(TraineeDetector.isTrainee(userType)){
            Mockito.doThrow(new EntityNotFoundException("Trainee not found"))
                    .when(traineeService).getTraineeByUsername(getProfileRequest);
        }else{
            Mockito.doThrow(new EntityNotFoundException("Trainer not found"))
                    .when(trainerService).getTrainerByUsername(getProfileRequest);
        }
    }

    @Then("{string} profile operation should throw EntityNotFoundException")
    public void should_throw_not_found(String userType){
        GetProfileRequest getProfileRequest = (GetProfileRequest) request;
        if (TraineeDetector.isTrainee(userType)){
            try{
                traineeService.getTraineeByUsername(getProfileRequest);
                Assert.fail("Expected EntityNotFoundException to be thrown");
            } catch (EntityNotFoundException e){
                // Expected exception
            }
        } else{
            try {
                trainerService.getTrainerByUsername(getProfileRequest);
                Assert.fail("Expected EntityNotFoundException to be thrown");
            }catch (EntityNotFoundException e){
                // Expected exception
            }
        }
    }
}
