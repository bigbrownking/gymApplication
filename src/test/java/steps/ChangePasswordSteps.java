package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.mockito.Mock;
import org.mockito.Mockito;
import steps.shared.RequestSteps;
import util.TraineeDetector;

public class ChangePasswordSteps {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    private Object request;

    public ChangePasswordSteps() {
        traineeService = Mockito.mock(TraineeService.class);
        trainerService = Mockito.mock(TrainerService.class);
    }

    @When("change password {string}")
    public void changePassword(String option) {
        request = RequestSteps.getRequest();
        System.out.println("Request set to: " + request);
        if (TraineeDetector.isTrainee(option)) {
            if (request instanceof ChangePasswordRequestDto) {
                changePasswordTrainee((ChangePasswordRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for change trainee password");
            }
        } else {
            if (request instanceof ChangePasswordRequestDto) {
                changePasswordTrainer((ChangePasswordRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for change trainer password");
            }
        }
    }

    private void changePasswordTrainee(ChangePasswordRequestDto changePasswordRequestDto) {
        traineeService.changePassword(changePasswordRequestDto);
    }

    private void changePasswordTrainer(ChangePasswordRequestDto changePasswordRequestDto) {
        trainerService.changePassword(changePasswordRequestDto);
    }


    @Then("the {string} password should be updated")
    public void password_should_be_updated(String userType) {
        if (TraineeDetector.isTrainee(userType)) {
            Mockito.verify(traineeService).changePassword((ChangePasswordRequestDto) request);
        } else {
            Mockito.verify(trainerService).changePassword((ChangePasswordRequestDto) request);
        }
    }
}
