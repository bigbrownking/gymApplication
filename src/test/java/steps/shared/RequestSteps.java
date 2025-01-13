package steps.shared;

import io.cucumber.java.en.Given;
import lombok.Getter;
import org.example.dto.requests.trainee.*;
import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.GetTrainerTrainingListRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.dto.requests.user.*;
import org.example.models.TrainingTypeEntity;
import org.example.service.TraineeService;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;

public class RequestSteps {
    @Mock
    protected TraineeService traineeService;

    @Getter
    private static Object request;

    public RequestSteps() {
        traineeService = Mockito.mock(TraineeService.class);
    }


    @Given("I have {string}")
    public void setup(String option) {
        cases(option);
        System.out.println("REQUEST IN FIRST STEP IS:" + request);
    }


    private void cases(String option) {
        switch (option) {
            case "valid activation request":
                request = new ActivateUserRequestDto("some.name", false);
                break;
            case "valid deactivation request":
                request = new DeactivateUserRequestDto("some.name", true);
                break;
            case "valid login request":
                request = new LoginRequestDto("valid", "valid");
                break;
            case "valid profile request":
                request = new GetProfileRequest("some.name");
                break;
            case "valid change password request":
                request = new ChangePasswordRequestDto("some.name", "oldPassword", "newPassword");
                break;
            case "valid trainee trainings request":
                request = new GetTraineeTrainingListRequestDto("some.name", LocalDateTime.of(2014, 10, 10, 10, 10, 10), null, null, null);
                break;
            case "valid trainer trainings request":
                request = new GetTrainerTrainingListRequestDto("some.name", LocalDateTime.of(2014, 10, 10, 10, 10, 10), null, null);
                break;
            case "valid trainee registration request":
                request = new CreateTraineeRequestDto("firstName", "lastName", LocalDateTime.now(), "address");
                break;
            case "valid trainer registration request":
                request = new CreateTrainerRequestDto("firstName", "lastName", new TrainingTypeEntity("Yoga"));
                break;
            case "valid trainee update request":
                request = new UpdateTraineeRequestDto("some.name", "some", "name", null, "newAddress", true);
                break;
            case "valid trainer update request":
                request = new UpdateTrainerRequestDto("some.name", "some", "name", new TrainingTypeEntity("Strength"), true);
                break;
            case "valid trainee delete request":
                request = new DeleteTraineeRequestDto("some.name");
                break;
            case "valid trainee not assigned trainers":
                request = new GetNotAssignedTrainersRequestDto("some.name");
                break;
            default:
                request = null;
                break;
        }
    }
}