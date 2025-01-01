package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.dto.requests.trainee.CreateTraineeRequestDto;
import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.responses.trainee.CreateTraineeResponseDto;
import org.example.dto.responses.trainer.CreateTrainerResponseDto;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.util.Generator;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import steps.shared.RequestSteps;
import util.TraineeDetector;

public class CreateSteps {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    private Object request;
    private Object response;

    public CreateSteps() {
        traineeService = Mockito.mock(TraineeService.class);
        trainerService = Mockito.mock(TrainerService.class);
    }

    @When("register {string}")
    public void register(String option) {
        request = RequestSteps.getRequest();
        System.out.println("Request set to: " + request);
        if (TraineeDetector.isTrainee(option)) {
            if (request instanceof CreateTraineeRequestDto) {
                response = createTrainee((CreateTraineeRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for create trainee");
            }
        } else {
            if (request instanceof CreateTrainerRequestDto) {
                response = createTrainer((CreateTrainerRequestDto) request);
            } else {
                throw new IllegalArgumentException("Invalid request type for create trainer");
            }
        }
    }
    private CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto createTraineeRequestDto){
        String generatedPassword = Generator.generatePassword();
        CreateTraineeResponseDto mockResponse = new CreateTraineeResponseDto("some.name", generatedPassword);
        Mockito.when(traineeService.createTrainee(createTraineeRequestDto)).thenReturn(mockResponse);
        return traineeService.createTrainee(createTraineeRequestDto);
    }

    private CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto createTrainerRequestDto){
        String generatedPassword = Generator.generatePassword();
        CreateTrainerResponseDto mockResponse = new CreateTrainerResponseDto("some.name", generatedPassword);
        Mockito.when(trainerService.createTrainer(createTrainerRequestDto)).thenReturn(mockResponse);
        return trainerService.createTrainer(createTrainerRequestDto);
    }

    @Then("the response should contain the registered {string} username and random password")
    public void user_should_be_created(String userType){
        Assert.assertNotNull("Response should not be null", response);

        if (TraineeDetector.isTrainee(userType)) {
            CreateTraineeResponseDto traineeResponse = (CreateTraineeResponseDto) response;

            Assert.assertEquals("some.name", traineeResponse.getUsername());
            Assert.assertNotNull("Password should not be null", traineeResponse.getPassword());

            Mockito.verify(traineeService).createTrainee((CreateTraineeRequestDto) request);
        } else{
            CreateTrainerResponseDto trainerResponse = (CreateTrainerResponseDto) response;

            Assert.assertEquals("some.name", trainerResponse.getUsername());
            Assert.assertNotNull("Password should not be null", trainerResponse.getPassword());

            Mockito.verify(trainerService).createTrainer((CreateTrainerRequestDto) request);

        }
    }



}

