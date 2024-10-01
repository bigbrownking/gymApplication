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
import org.example.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainee")
@Tag(name = "Trainee", description = "API for managing Trainees")
public class TraineeController {

    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Operation(summary = "Register a new trainee", description = "Create a new trainee profile", tags = { "Trainee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainee created successfully",
                    content = @Content(schema = @Schema(implementation = CreateTraineeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<CreateTraineeResponseDto> registerTrainee(@RequestBody CreateTraineeRequestDto createTraineeRequestDto) {
        try {
            CreateTraineeResponseDto response = traineeService.createTrainee(createTraineeRequestDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Login trainee", description = "Login a trainee using username and password", tags = { "Trainee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            traineeService.getTraineeByUsernameAndPassword(loginRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Update trainee details", description = "Update an existing trainee's information", tags = { "Trainee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee updated successfully",
                    content = @Content(schema = @Schema(implementation = UpdateTraineeResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update")
    public ResponseEntity<UpdateTraineeResponseDto> updateTrainee(@RequestBody UpdateTraineeRequestDto updateTraineeRequestDto) {
        try {
            UpdateTraineeResponseDto response = traineeService.updateTrainee(updateTraineeRequestDto);
            if (response == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a trainee", description = "Delete an existing trainee profile", tags = { "Trainee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trainee deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTrainee(@RequestBody DeleteTraineeRequestDto deleteTraineeRequestDto) {
        try {
            traineeService.deleteTrainee(deleteTraineeRequestDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get trainers not assigned to a trainee", description = "Retrieve a list of trainers who are not assigned to a specific trainee", tags = { "Trainee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully",
                    content = @Content(schema = @Schema(implementation = GetNotAssignedTrainersResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/notAssignedTrainers")
    public ResponseEntity<GetNotAssignedTrainersResponseDto> getNotAssignedTrainers(@RequestBody GetNotAssignedTrainersRequestDto getNotAssignedTrainersRequestDto) {
        try {
            GetNotAssignedTrainersResponseDto response = traineeService.getTrainersNotAssignedToTrainee(getNotAssignedTrainersRequestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get trainee profile by username", description = "Retrieve a trainee's profile using their username", tags = { "Trainee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = GetTraineeByUsernameResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Trainee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/profile")
    public ResponseEntity<GetTraineeByUsernameResponseDto> getTraineeByUsername(@RequestBody GetTraineeByUsernameRequestDto getTraineeByUsernameRequestDto) {
        try {
            GetTraineeByUsernameResponseDto response = traineeService.getTraineeByUsername(getTraineeByUsernameRequestDto);
            if (response == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get trainee's training list", description = "Retrieve a list of trainings assigned to the trainee", tags = { "Trainee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training list retrieved successfully",
                    content = @Content(schema = @Schema(implementation = GetTraineeTrainingListResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/trainings")
    public ResponseEntity<GetTraineeTrainingListResponseDto> getTrainings(@RequestBody GetTraineeTrainingListRequestDto getTraineeTrainingListRequestDto) {
        try {
            GetTraineeTrainingListResponseDto response = traineeService.getTrainingByCriteria(getTraineeTrainingListRequestDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Activate a trainee", description = "Activate a trainee account", tags = { "Trainee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee activated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/activate")
    public ResponseEntity<Void> activateTrainee(@RequestBody ActivateUserRequestDto activateUserRequestDto) {
        try {
            traineeService.activateTrainee(activateUserRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deactivate a trainee", description = "Deactivate a trainee account", tags = { "Trainee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee deactivated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivateTrainee(@RequestBody DeactivateUserRequestDto deactivateUserRequestDto) {
        try {
            traineeService.deactivateTrainee(deactivateUserRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Change trainee password", description = "Change a trainee's password", tags = { "Trainee" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        try {
            traineeService.changePassword(changePasswordRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
