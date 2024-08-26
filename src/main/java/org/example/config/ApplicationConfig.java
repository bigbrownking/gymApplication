package org.example.config;

import org.example.facade.TrainingFacade;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.example")
public class ApplicationConfig {

    @Bean
    public TrainingFacade trainingFacade(TrainerService trainerService,
                                         TraineeService traineeService,
                                         TrainingService trainingService){
        return new TrainingFacade(trainerService, traineeService, trainingService);
    }
}
