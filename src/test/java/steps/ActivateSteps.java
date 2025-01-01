package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.mockito.Mock;
import org.mockito.Mockito;
import steps.shared.RequestSteps;
import util.TraineeDetector;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ActivateSteps {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    private Object request;

    public ActivateSteps() {
        traineeService = Mockito.mock(TraineeService.class);
        trainerService = Mockito.mock(TrainerService.class);
    }

    @When("activate {string}")
    public void activate(String option) {
        request = RequestSteps.getRequest();
        System.out.println("Request set to: " + request);
        if (TraineeDetector.isTrainee(option)) {
            if (request instanceof ActivateUserRequestDto) {
                activateTrainee((ActivateUserRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for trainee activation");
            }
        } else {
            if (request instanceof ActivateUserRequestDto) {
                activateTrainer((ActivateUserRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for trainer activation");
            }
        }
    }
    private void activateTrainee(ActivateUserRequestDto activateUserRequestDto) {
        traineeService.activateTrainee(activateUserRequestDto);
    }

    private void activateTrainer(ActivateUserRequestDto activateUserRequestDto) {
        trainerService.activateTrainer(activateUserRequestDto);
    }

    @Then("the {string} account should be activated")
    public void account_should_be_activated(String userType) {
        if (TraineeDetector.isTrainee(userType)) {
            Mockito.verify(traineeService).activateTrainee((ActivateUserRequestDto) request);
        }  else {
            Mockito.verify(trainerService).activateTrainer((ActivateUserRequestDto) request);
        }
    }
}
