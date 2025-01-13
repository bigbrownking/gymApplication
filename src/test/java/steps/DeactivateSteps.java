package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.mockito.Mock;
import org.mockito.Mockito;
import steps.shared.RequestSteps;
import util.TraineeDetector;

public class DeactivateSteps {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    private final Object request;

    public DeactivateSteps() {
        traineeService = Mockito.mock(TraineeService.class);
        trainerService = Mockito.mock(TrainerService.class);
        request = RequestSteps.getRequest();
    }

    @When("deactivate {string}")
    public void deactivate(String option){
        if (TraineeDetector.isTrainee(option)) {
            if (request instanceof DeactivateUserRequestDto) {
                deactivateTrainee((DeactivateUserRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for trainee deactivation");
            }
        } else {
            if (request instanceof DeactivateUserRequestDto) {
                deactivateTrainer((DeactivateUserRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for trainer deactivation");
            }
        }
    }
    private void deactivateTrainee(DeactivateUserRequestDto deactivateUserRequestDto) {
        traineeService.deactivateTrainee(deactivateUserRequestDto);
    }

    private void deactivateTrainer(DeactivateUserRequestDto deactivateUserRequestDto) {
        trainerService.deactivateTrainer(deactivateUserRequestDto);
    }
    @Then("the {string} account should be deactivated")
    public void account_should_be_activated(String userType) {
        if (userType.equalsIgnoreCase("trainee")) {
            Mockito.verify(traineeService).deactivateTrainee((DeactivateUserRequestDto) request);
        } else if (userType.equalsIgnoreCase("trainer")) {
            Mockito.verify(trainerService).deactivateTrainer((DeactivateUserRequestDto) request);
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
}
