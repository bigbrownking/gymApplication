package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.requests.training.CreateTrainingRequestDto;
import org.example.dto.responses.training.GetTrainingTypesResponseDto;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training")
@Tag(name = "Training", description = "API for managing Trainings")
public class TrainingController {

    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get available training types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved training types",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetTrainingTypesResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/types")
    public ResponseEntity<GetTrainingTypesResponseDto> getTrainingTypes() {
        GetTrainingTypesResponseDto response = trainingService.getTrainingTypes();
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Register a new training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training successfully registered"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<Void> addTraining(@RequestBody CreateTrainingRequestDto createTrainingRequestDto) {
        trainingService.createTraining(createTrainingRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
