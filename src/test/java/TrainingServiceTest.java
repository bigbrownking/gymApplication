

import org.example.dao.TrainingDao;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.example.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTraining_ShouldCreateTraining() {
        // Arrange
        Training training = new Training();
        training.setTrainingType(new TrainingTypeEntity("Type1"));

        // Act
        trainingService.createTraining(training);

        // Assert
        verify(trainingDao).create(training);
    }

    @Test
    void getTraining_ShouldReturnTraining_WhenExists() {
        // Arrange
        Training training = new Training();
        Long trainingId = 1L;
        when(trainingDao.select(trainingId)).thenReturn(training);

        // Act
        Training result = trainingService.getTraining(trainingId);

        // Assert
        assertThat(result).isEqualTo(training);
    }

    @Test
    void getTraining_ShouldReturnNull_WhenNotExists() {
        // Arrange
        Long trainingId = 1L;
        when(trainingDao.select(trainingId)).thenReturn(null);

        // Act
        Training result = trainingService.getTraining(trainingId);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void getAllTrainings_ShouldReturnListOfTrainings() {
        // Arrange
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training());
        trainings.add(new Training());
        when(trainingDao.listAll()).thenReturn(trainings);

        // Act
        List<Training> result = trainingService.getAllTrainings();

        // Assert
        assertThat(result).isEqualTo(trainings);
    }

    @Test
    void updateTraining_ShouldUpdateTraining() {
        // Arrange
        Training training = new Training();

        // Act
        trainingService.updateTraining(training);

        // Assert
        verify(trainingDao).updateTraining(training);
    }

    @Test
    void getTrainingType_ShouldReturnTrainingTypeEntity_WhenExists() {
        // Arrange
        TrainingTypeEntity trainingTypeEntity = new TrainingTypeEntity("Type1");
        String trainingTypeName = "Type1";
        when(trainingDao.getTrainingType(trainingTypeName)).thenReturn(trainingTypeEntity);

        // Act
        TrainingTypeEntity result = trainingService.getTrainingType(trainingTypeName);

        // Assert
        assertThat(result).isEqualTo(trainingTypeEntity);
    }

    @Test
    void getTrainingType_ShouldReturnNull_WhenNotExists() {
        // Arrange
        String trainingTypeName = "Type1";
        when(trainingDao.getTrainingType(trainingTypeName)).thenReturn(null);

        // Act
        TrainingTypeEntity result = trainingService.getTrainingType(trainingTypeName);

        // Assert
        assertThat(result).isNull();
    }
}
