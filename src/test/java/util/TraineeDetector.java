package util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TraineeDetector {
    public boolean isTrainee(String option){
        return option.equals("trainee");
    }

}
