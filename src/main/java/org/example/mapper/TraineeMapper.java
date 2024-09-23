package org.example.mapper;

import org.example.dto.TraineeDto;
import org.example.mapper.trainee.ToTraineeDtoMapper;
import org.example.mapper.trainee.ToTraineeMapper;
import org.example.models.Trainee;

import java.util.List;

public interface TraineeMapper extends ToTraineeMapper, ToTraineeDtoMapper {
    List<TraineeDto> convertTraineesToDto(List<Trainee> trainees);

}
