package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.trainee.DeleteTraineeRequestDto;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.mockito.Mock;
import org.mockito.Mockito;
import steps.shared.RequestSteps;

public class DeleteTraineeSteps {
    @Mock
    private TraineeService traineeService;
    private Object request;

    public DeleteTraineeSteps() {
        traineeService = Mockito.mock(TraineeService.class);
    }

    @When("delete trainee")
    public void deleteTrainee() {
        request = RequestSteps.getRequest();
        if (request instanceof DeleteTraineeRequestDto) {
            traineeService.deleteTrainee((DeleteTraineeRequestDto) request);
        } else {
            throw new IllegalArgumentException("Invalid request type for trainee deletion");
        }
    }

    @Then("the trainee should be deleted from the system")
    public void trainee_should_be_deleted(){
        Mockito.verify(traineeService).deleteTrainee((DeleteTraineeRequestDto) request);
    }

}
