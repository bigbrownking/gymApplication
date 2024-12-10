package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.requests.trainee.*;
import org.example.dto.requests.user.*;
import org.example.dto.responses.trainee.*;
import org.example.models.TrainingTypeEntity;
import org.example.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/trainee")
@Tag(name = "Trainee", description = "API for managing Trainees")
public class TraineeController {

    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }


    @Operation(summary = "Register a new trainee", description = "Create a new trainee profile", tags = {"Trainee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee created successfully",
                    content = @Content(schema = @Schema(implementation = CreateTraineeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<CreateTraineeResponseDto> registerTrainee(@RequestBody CreateTraineeRequestDto createTraineeRequestDto) {
        CreateTraineeResponseDto response = traineeService.createTrainee(createTraineeRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update trainee details", description = "Update an existing trainee's information", tags = {"Trainee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee updated successfully",
                    content = @Content(schema = @Schema(implementation = UpdateTraineeResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update")
    public ResponseEntity<UpdateTraineeResponseDto> updateTrainee(
            @RequestBody UpdateTraineeRequestDto updateTraineeRequestDto) {
        UpdateTraineeResponseDto response = traineeService.updateTrainee(updateTraineeRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


   @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete a trainee", description = "Delete an existing trainee profile", tags = {"Trainee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTrainee(@RequestBody DeleteTraineeRequestDto deleteTraineeRequestDto) {
        traineeService.deleteTrainee(deleteTraineeRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Get trainers not assigned to a trainee", description = "Retrieve a list of trainers who are not assigned to a specific trainee", tags = {"Trainee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully",
                    content = @Content(schema = @Schema(implementation = GetNotAssignedTrainersResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/notAssignedTrainers")
    public ResponseEntity<GetNotAssignedTrainersResponseDto> getNotAssignedTrainers(
            @RequestParam String username) {
        GetNotAssignedTrainersRequestDto getNotAssignedTrainersRequestDto = new GetNotAssignedTrainersRequestDto();
        getNotAssignedTrainersRequestDto.setUsername(username);
        GetNotAssignedTrainersResponseDto response = traineeService.getTrainersNotAssignedToTrainee(getNotAssignedTrainersRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get trainee profile by username", description = "Retrieve a trainee's profile using their username", tags = {"Trainee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = GetTraineeByUsernameResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/profile")
    public ResponseEntity<GetTraineeByUsernameResponseDto> getTraineeByUsername(
            @RequestParam String username) {
        GetTraineeByUsernameRequestDto getTraineeByUsernameRequestDto = new GetTraineeByUsernameRequestDto();
        getTraineeByUsernameRequestDto.setUsername(username);
        GetTraineeByUsernameResponseDto response = traineeService.getTraineeByUsername(getTraineeByUsernameRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get trainee's training list", description = "Retrieve a list of trainings assigned to the trainee", tags = {"Trainee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training list retrieved successfully",
                    content = @Content(schema = @Schema(implementation = GetTraineeTrainingListResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/trainings")
    public ResponseEntity<GetTraineeTrainingListResponseDto> getTrainings(
            @RequestParam  String username,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) TrainingTypeEntity trainingType) {
        GetTraineeTrainingListRequestDto getTraineeTrainingListRequestDto = new GetTraineeTrainingListRequestDto();
        getTraineeTrainingListRequestDto.setUsername(username);
        getTraineeTrainingListRequestDto.setTrainingType(trainingType);
        getTraineeTrainingListRequestDto.setFromDate(fromDate);
        getTraineeTrainingListRequestDto.setTrainerName(trainerName);
        getTraineeTrainingListRequestDto.setToDate(toDate);
        GetTraineeTrainingListResponseDto response = traineeService.getTrainingByCriteria(getTraineeTrainingListRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Activate a trainee", description = "Activate a trainee account", tags = {"Trainee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee activated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/activate")
    public ResponseEntity<Void> activateTrainee(
            @RequestBody ActivateUserRequestDto activateUserRequestDto) {
        traineeService.activateTrainee(activateUserRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Deactivate a trainee", description = "Deactivate a trainee account", tags = {"Trainee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee deactivated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivateTrainee(
            @RequestBody DeactivateUserRequestDto deactivateUserRequestDto) {
        traineeService.deactivateTrainee(deactivateUserRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Change trainee password", description = "Change a trainee's password", tags = {"Trainee"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        traineeService.changePassword(changePasswordRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
