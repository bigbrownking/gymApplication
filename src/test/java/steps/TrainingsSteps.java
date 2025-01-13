package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.trainee.GetTraineeTrainingListRequestDto;
import org.example.dto.requests.trainer.GetTrainerTrainingListRequestDto;
import org.example.dto.responses.trainee.GetTraineeTrainingListResponseDto;
import org.example.dto.responses.trainer.GetTrainerTrainingListResponseDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import steps.shared.RequestSteps;
import util.SettingsTestData;
import util.TraineeDetector;

import java.util.Optional;

public class TrainingsSteps {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    private Object request;
    private Object response;

    public TrainingsSteps() {
        traineeService = Mockito.mock(TraineeService.class);
        trainerService = Mockito.mock(TrainerService.class);
    }


    @When("trainings {string}")
    public void trainings(String option){
        request = RequestSteps.getRequest();
        System.out.println("Request set to: " + request);
        if (TraineeDetector.isTrainee(option)) {
            if (request instanceof GetTraineeTrainingListRequestDto) {
                response = trainingsTrainee((GetTraineeTrainingListRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for trainings trainee");
            }
        } else {
            if (request instanceof GetTrainerTrainingListRequestDto) {
                response = trainingsTrainer((GetTrainerTrainingListRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for trainings trainer");
            }
        }
    }

    private GetTraineeTrainingListResponseDto trainingsTrainee(GetTraineeTrainingListRequestDto getTraineeTrainingListRequestDto){
        GetTraineeTrainingListResponseDto mockResponse = new GetTraineeTrainingListResponseDto(SettingsTestData.getTrainingList());
        Mockito.when(traineeService.getTrainingByCriteria(getTraineeTrainingListRequestDto)).thenReturn(mockResponse);
        return traineeService.getTrainingByCriteria(getTraineeTrainingListRequestDto);
    }

    private GetTrainerTrainingListResponseDto trainingsTrainer(GetTrainerTrainingListRequestDto getTrainerTrainingListRequestDto){
        GetTrainerTrainingListResponseDto mockResponse = new GetTrainerTrainingListResponseDto(SettingsTestData.getTrainingList());
        Mockito.when(trainerService.getTrainingByCriteria(getTrainerTrainingListRequestDto)).thenReturn(mockResponse);
        return trainerService.getTrainingByCriteria(getTrainerTrainingListRequestDto);
    }

    @Then("the response should contain trainings list of {string} matching the criteria")
    public void training_list_should_be_retrieved(String userType) {
        Assert.assertNotNull("Response should not be null", response);

        if (TraineeDetector.isTrainee(userType)) {
            GetTraineeTrainingListResponseDto traineeResponse = (GetTraineeTrainingListResponseDto) response;

            Assert.assertEquals(3, traineeResponse.getTrainingDtos().size());

            Assert.assertEquals("Chest and Triceps Workout", traineeResponse.getTrainingDtos().get(0).getTrainingName());
            Assert.assertEquals("Alex Johnson", traineeResponse.getTrainingDtos().get(0).getTraineeName());

            Assert.assertEquals(75, (int) traineeResponse.getTrainingDtos().get(1).getDuration());
            Mockito.verify(traineeService).getTrainingByCriteria((GetTraineeTrainingListRequestDto) request);
        } else{
            Mockito.verify(trainerService).getTrainingByCriteria((GetTrainerTrainingListRequestDto) request);
        }
    }
}
