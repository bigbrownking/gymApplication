package org.example.dao.impl;

import org.example.dao.TraineeDao;
import org.example.models.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TraineeDaoImpl implements TraineeDao {
    private Map<Long, Trainee> traineeMap;

    @Autowired
    public void setTraineeMap(Map<Long, Trainee> traineeMap) {
        this.traineeMap = traineeMap;
    }

    public void create(Trainee trainee) {
        traineeMap.put(trainee.getUserId(), trainee);
    }

    public void update(Trainee trainee) {
        traineeMap.put(trainee.getUserId(), trainee);
    }

    public void delete(Long userId) {
        traineeMap.remove(userId);
    }

    public List<Trainee> listAll() {
        return new ArrayList<>(traineeMap.values());
    }

    public Optional<Trainee> findByUsername(String username) {
        return traineeMap.values().stream()
                .filter(trainee -> username.equals(trainee.getUsername()))
                .findFirst();
    }

}
