package org.example.dao.impl;

import org.example.dao.TrainingDao;
import org.example.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainingDaoImpl implements TrainingDao {
    private Map<Long, Training> trainingMap;

    @Autowired
    public TrainingDaoImpl(Map<Long, Training> trainingMap) {
        this.trainingMap = trainingMap;
    }

    public void create(Training training) {
        trainingMap.put(training.getTrainingId(), training);
    }

    public Training select(Long trainingId) {
        return trainingMap.get(trainingId);
    }

    public List<Training> listAll() {
        return new ArrayList<>(trainingMap.values());
    }

    public void updateTraining(Training training) {
        trainingMap.put(training.getTrainingId(), training);
    }
}
