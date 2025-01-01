package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.GetProfileRequest;
import org.example.dto.responses.trainee.GetTraineeByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.models.TrainingTypeEntity;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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

    private GetTraineeByUsernameResponseDto profileTrainee(GetProfileRequest getProfileRequest) {
        GetTraineeByUsernameResponseDto mockResponse = new GetTraineeByUsernameResponseDto(
                "some", "name", LocalDateTime.now(), "address", true, null);
        Mockito.when(traineeService.getTraineeByUsername(getProfileRequest)).thenReturn(mockResponse);
        return traineeService.getTraineeByUsername(getProfileRequest);
    }

    private GetTrainerByUsernameResponseDto profileTrainer(GetProfileRequest getProfileRequest) {
        GetTrainerByUsernameResponseDto mockResponse = new GetTrainerByUsernameResponseDto(
                "some", "name", new TrainingTypeEntity("Yoga"), true, null);
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
            Assert.assertNull(traineeResponse.getTrainerDtos());

            Mockito.verify(traineeService).getTraineeByUsername((GetProfileRequest) request);

        } else {
            GetTrainerByUsernameResponseDto trainerResponse = (GetTrainerByUsernameResponseDto) response;

            Assert.assertEquals("some", trainerResponse.getFirstname());
            Assert.assertEquals("name", trainerResponse.getLastname());
            Assert.assertTrue(trainerResponse.isActive());
            Assert.assertNull(trainerResponse.getTraineeDtos());

            Mockito.verify(trainerService).getTrainerByUsername((GetProfileRequest) request);
        }
    }
}
