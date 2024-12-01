package org.example.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.TrainingDao;
import org.example.dto.requests.trainer.GetWorkloadRequest;
import org.example.dto.requests.training.CreateTrainingRequestDto;
import org.example.dto.responses.trainer.WorkloadResponse;
import org.example.dto.responses.training.GetTrainingTypesResponseDto;
import org.example.exceptions.InvalidDataException;
import org.example.mapper.TrainingMapper;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.example.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingDao trainingDao;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final TrainingMapper trainingMapper;
    private final RestTemplate restTemplate;

    @Value("${secondary.microservice.url}")
    private String secondaryMicroserviceUrl;


    @Override
    @CircuitBreaker(name = "workloadService", fallbackMethod = "processWorkLoadFallback")
    public void createTraining(CreateTrainingRequestDto createTrainingRequestDto) {
        try {
            if (createTrainingRequestDto == null) {
                LOGGER.warn("Invalid request...");
                return;
            }
            LOGGER.info("Creating new training...");
            Trainee trainee = traineeDao.findByUsername(createTrainingRequestDto.getTraineeUsername()).orElse(null);
            Trainer trainer = trainerDao.findByUsername(createTrainingRequestDto.getTrainerUsername()).orElse(null);

            if (trainee == null) {
                LOGGER.warn("No such trainee found...");
                return;
            }
            if (trainer == null) {
                LOGGER.warn("No such trainer found...");
                return;
            }

            Training training = trainingMapper.toTraining(createTrainingRequestDto, trainee, trainer);
            trainingDao.create(training);

            GetWorkloadRequest getWorkloadRequest = new GetWorkloadRequest();
            getWorkloadRequest.setUsername(trainer.getUsername());
            getWorkloadRequest.setFirstName(trainer.getFirstName());
            getWorkloadRequest.setLastName(trainer.getLastName());
            getWorkloadRequest.setTrainingDate(training.getTrainingDate());
            getWorkloadRequest.setDuration(training.getTrainingDuration());
            getWorkloadRequest.setActive(trainer.isActive());
            getWorkloadRequest.setAction("ADD");

            updateWorkload(getWorkloadRequest);


        } catch (InvalidDataException e) {
            LOGGER.warn("Data is invalid...");
        }
    }
    private void updateWorkload(GetWorkloadRequest getWorkloadRequest) {
        try {
            String url = secondaryMicroserviceUrl + "/workload";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<GetWorkloadRequest> requestEntity = new HttpEntity<>(getWorkloadRequest, headers);

            ResponseEntity<?> response = restTemplate.postForEntity(url, requestEntity, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Successfully updated workload in Secondary Microservice");
            } else {
                LOGGER.error("Failed to update workload. Response: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            LOGGER.error("Error while calling Secondary Microservice: {}", e.getMessage());
        }
    }
    private WorkloadResponse processWorkLoadFallback(GetWorkloadRequest getWorkloadRequest, Throwable throwable) {
        WorkloadResponse response = new WorkloadResponse();
        response.setUsername(getWorkloadRequest.getUsername());
        response.setFirstName("N/A");
        response.setLastName("N/A");
        response.setActive(false);
        response.setTrainingSummary(Collections.emptyMap());
        return response;
    }
    @Override
    public GetTrainingTypesResponseDto getTrainingTypes() {
        try {
            LOGGER.info("Retrieving all training types...");
            List<TrainingTypeEntity> trainingTypeEntities = trainingDao.getTrainingTypes();
            return trainingMapper.toGetTrainingTypesDto(trainingTypeEntities);
        } catch (InvalidDataException e) {
            LOGGER.warn("Data is invalid...");
            return null;
        }
    }
}
