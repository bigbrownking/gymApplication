package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.trainee.DeleteTraineeRequestDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.service.TraineeService;
import org.junit.Assert;
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

    @And("trainee doesn't exist")
    public void doesnt_exist(){
        DeleteTraineeRequestDto deleteRequest = (DeleteTraineeRequestDto) request;
        Mockito.doThrow(new EntityNotFoundException("Trainee not found"))
                .when(traineeService).deleteTrainee(deleteRequest);
    }
    @Then("delete operation should throw EntityNotFoundException")
    public void should_throw_not_found() {
        DeleteTraineeRequestDto deleteRequest = (DeleteTraineeRequestDto) request;

        try {
            traineeService.deleteTrainee(deleteRequest);
            Assert.fail("Expected EntityNotFoundException to be thrown");
        } catch (EntityNotFoundException e) {
            //pass
        }
    }

}
