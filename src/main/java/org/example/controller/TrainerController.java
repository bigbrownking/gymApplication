package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.requests.trainer.*;
import org.example.dto.requests.user.*;
import org.example.dto.responses.trainer.*;
import org.example.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
@Tag(name = "Trainer", description = "API for managing Trainers")
public class TrainerController {

    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }


    @Operation(summary = "Register a new trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trainer successfully registered",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateTrainerResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<CreateTrainerResponseDto> registerTrainer(@RequestBody CreateTrainerRequestDto createTrainerRequestDto) {
        CreateTrainerResponseDto response = trainerService.createTrainer(createTrainerRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get trainer by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetTrainerByUsernameResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/profile")
    public ResponseEntity<GetTrainerByUsernameResponseDto> getTrainerByUsername(
            @RequestBody GetTrainerByUsernameRequestDto getTrainerByUsernameRequestDto) {
        GetTrainerByUsernameResponseDto response = trainerService.getTrainerByUsername(getTrainerByUsernameRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get trainer's trainings by criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainings found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetTrainerTrainingListResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/trainings")
    public ResponseEntity<GetTrainerTrainingListResponseDto> getTrainings(
            @RequestBody GetTrainerTrainingListRequestDto getTrainerTrainingListRequestDto) {
        GetTrainerTrainingListResponseDto response = trainerService.getTrainingByCriteria(getTrainerTrainingListRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update trainer profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer profile updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdateTrainerResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/update")
    public ResponseEntity<UpdateTrainerResponseDto> updateTrainer(
            @RequestBody UpdateTrainerRequestDto updateTrainerRequestDto) {
        UpdateTrainerResponseDto response = trainerService.updateTrainer(updateTrainerRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Activate a trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer activated"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PatchMapping("/activate")
    public ResponseEntity<Void> activateTrainer(
            @RequestBody ActivateUserRequestDto activateUserRequestDto) {
        trainerService.activateTrainer(activateUserRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Deactivate a trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer deactivated"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivateTrainer(
            @RequestBody DeactivateUserRequestDto deactivateUserRequestDto) {
        trainerService.deactivateTrainer(deactivateUserRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Change trainer password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        trainerService.changePassword(changePasswordRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
