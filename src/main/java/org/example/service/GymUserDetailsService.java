package org.example.service;

import org.example.dto.requests.trainer.GetTrainerByUsernameRequestDto;
import org.example.dto.requests.trainee.GetTraineeByUsernameRequestDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.dto.responses.trainee.GetTraineeByUsernameResponseDto;
import org.example.models.GymUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GymUserDetailsService implements UserDetailsService {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(GymUserDetailsService.class);

    @Autowired
    public GymUserDetailsService(TrainerService trainerService,
                                 TraineeService traineeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        GetTrainerByUsernameRequestDto trainerRequestDto = new GetTrainerByUsernameRequestDto();
        trainerRequestDto.setUsername(username);

        try {
            GetTrainerByUsernameResponseDto trainerResponse = trainerService.getTrainerByUsername(trainerRequestDto);
            if (trainerResponse != null) {
                return new GymUserDetails(
                        username,
                        trainerService.getPasswordFromTrainer(username),
                        List.of(() -> "ROLE_TRAINER"),
                        trainerResponse.isActive());
            }

        } catch (Exception e) {
            LOGGER.warn("Trainer not found...");
        }

        GetTraineeByUsernameRequestDto traineeRequestDto = new GetTraineeByUsernameRequestDto();
        traineeRequestDto.setUsername(username);

        try {
            GetTraineeByUsernameResponseDto traineeResponse = traineeService.getTraineeByUsername(traineeRequestDto);
            if (traineeResponse != null) {
                return new GymUserDetails(
                        username,
                        traineeService.getPasswordFromTrainee(username),
                        List.of(() -> "ROLE_TRAINEE"),
                        traineeResponse.isActive());
            }
        } catch (Exception e) {
            LOGGER.warn("Trainee not found...");
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
