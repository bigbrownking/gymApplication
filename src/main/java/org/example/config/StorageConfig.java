package org.example.config;

import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.util.LogUtil;
import org.example.util.SettingsDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
public class StorageConfig implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageConfig.class);

    @Value("${trainer.data.file}")
    private Resource trainerDataFile;

    @Value("${trainee.data.file}")
    private Resource traineeDataFile;

    @Value("${training.data.file}")
    private Resource trainingDataFile;

    @Bean
    public Map<Long, Trainer> trainerMap(){
        LOGGER.info("Populating maps...");
        return new HashMap<>();
    }
    @Bean
    public Map<Long, Trainee> traineeMap(){
        LOGGER.info("Populating maps...");
        return new HashMap<>();
    }
    @Bean
    public Map<Long, Training> trainingMap(){
        LOGGER.info("Populating maps...");
        return new HashMap<>();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try {
            switch (beanName) {
                case "trainerMap":
                    populateMap((Map<Long, Trainer>) bean, trainerDataFile, Trainer.class);
                    break;
                case "traineeMap":
                    populateMap((Map<Long, Trainee>) bean, traineeDataFile, Trainee.class);
                    break;
                case "trainingMap":
                    populateMap((Map<Long, Training>) bean, trainingDataFile, Training.class);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            LOGGER.error("Reading from file error!", e);
            throw new RuntimeException("Failed to load data from file: " + beanName, e);
        }
        return bean;
    }

    private <T> void populateMap(Map<Long, T> map, Resource resource, Class<T> clazz) throws IOException {
        Map<Long, T> data = SettingsDataUtil.readJsonToMap(resource, clazz);
        map.putAll(data);
    }

}
