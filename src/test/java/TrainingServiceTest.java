import org.example.dao.TrainingDao;
import org.example.models.Training;
import org.example.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

public class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateTrainingSuccessfully() {
        Training training = new Training();
        training.setTrainingId(1L);

        trainingService.createTraining(training);

        verify(trainingDao, times(1)).create(training);
    }

    @Test
    public void givenId_whenGetTrainerById_thenSuccess() {
        Training training = new Training();
        training.setTrainingId(1L);
        when(trainingDao.select(anyLong())).thenReturn(training);

        Training result = trainingService.getTraining(1L);

        verify(trainingDao, times(1)).select(anyLong());
        assertThat(result).isEqualTo(training);
    }

    @Test
    public void getAllTrainingsSuccessfully() {
        Training training1 = new Training();
        Training training2 = new Training();
        List<Training> trainings = Arrays.asList(training1, training2);
        when(trainingDao.listAll()).thenReturn(trainings);

        List<Training> result = trainingService.getAllTrainings();

        verify(trainingDao, times(1)).listAll();
        assertThat(result).isEqualTo(trainings);
    }

    @Test
    public void shouldUpdateTrainingSuccessfully() {
        Training training = new Training();
        training.setTrainingId(1L);

        trainingService.updateTraining(training);

        verify(trainingDao, times(1)).updateTraining(training);
    }
}
