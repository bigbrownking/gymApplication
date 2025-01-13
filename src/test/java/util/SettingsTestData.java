package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

@UtilityClass
public class SettingsTestData {
    public final String RESOURCES_PATH = "src/test/resources/";
    public final String TEST_DATA_PATH = RESOURCES_PATH + "testdata/";
    public final String TRAININGS_LIST_FILE_PATH = TEST_DATA_PATH + "trainings.json";
    public final String NOT_ASSIGNED_TRAINERS_FILE_PATH = TEST_DATA_PATH + "notAssignedTrainers.json";
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public List<TrainingDto> getTrainingList() {
        try {
            return objectMapper.readValue(
                    new File(TRAININGS_LIST_FILE_PATH),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, TrainingDto.class)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize trainings list: " + e.getMessage(), e);
        }
    }

    public List<TrainerDto> getNotAssignedTrainers() {
        try {
            return objectMapper.readValue(
                    new File(NOT_ASSIGNED_TRAINERS_FILE_PATH),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, TrainerDto.class)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize not assigned trainers list: " + e.getMessage(), e);
        }
    }

}
