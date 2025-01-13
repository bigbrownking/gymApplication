package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.trainee.DeleteTraineeRequestDto;
import org.example.dto.requests.trainee.GetNotAssignedTrainersRequestDto;
import org.example.dto.requests.trainee.GetTraineeTrainingListRequestDto;
import org.example.dto.responses.trainee.GetNotAssignedTrainersResponseDto;
import org.example.service.TraineeService;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import steps.shared.RequestSteps;
import util.SettingsTestData;

public class TraineeNotAssignedTrainersSteps {
    @Mock
    private TraineeService traineeService;
    private Object request;
    private Object response;

    public TraineeNotAssignedTrainersSteps() {
        traineeService = Mockito.mock(TraineeService.class);
    }

    @When("not assigned trainers")
    public void notAssignedTrainers(){
        request = RequestSteps.getRequest();
        if (request instanceof GetNotAssignedTrainersRequestDto) {
            response = notAssignedTrainers((GetNotAssignedTrainersRequestDto) request);
        } else {
            throw new IllegalArgumentException("Invalid request type for not assigned trainers of trainee");
        }
    }

    private GetNotAssignedTrainersResponseDto notAssignedTrainers(GetNotAssignedTrainersRequestDto getNotAssignedTrainersRequestDto){
        GetNotAssignedTrainersResponseDto mockResponse = new GetNotAssignedTrainersResponseDto(SettingsTestData.getNotAssignedTrainers());
        Mockito.when(traineeService.getTrainersNotAssignedToTrainee(getNotAssignedTrainersRequestDto)).thenReturn(mockResponse);
        return traineeService.getTrainersNotAssignedToTrainee(getNotAssignedTrainersRequestDto);

    }

    @Then("the response should contain a list of trainers not assigned to the trainee")
    public void should_contain_list_of_not_assigned_trainers(){
        Assert.assertNotNull("Response should not be null", response);
        GetNotAssignedTrainersResponseDto traineeResponse = (GetNotAssignedTrainersResponseDto) response;

        Assert.assertEquals(3, traineeResponse.getTrainerDtos().size());

        Assert.assertEquals("trainer_alex", traineeResponse.getTrainerDtos().get(0).getUsername());
        Assert.assertEquals("Strength Training", traineeResponse.getTrainerDtos().get(0).getSpecialization().getTrainingTypeName());


        Mockito.verify(traineeService).getTrainersNotAssignedToTrainee((GetNotAssignedTrainersRequestDto) request);
    }
}
