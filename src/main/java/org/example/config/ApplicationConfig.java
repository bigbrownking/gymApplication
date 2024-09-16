package org.example.config;

import org.example.facade.TrainingFacade;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = "org.example")
public class ApplicationConfig {

    @Bean
    public TrainingFacade trainingFacade(TrainerService trainerService,
                                         TraineeService traineeService,
                                         TrainingService trainingService){
        return new TrainingFacade(trainerService, traineeService, trainingService);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
