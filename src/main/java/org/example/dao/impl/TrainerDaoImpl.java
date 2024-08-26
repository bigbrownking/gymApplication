package org.example.dao.impl;

import org.example.dao.TrainerDao;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TrainerDaoImpl implements TrainerDao {
    private Map<Long, Trainer> trainerMap;

    @Autowired
    public void setTrainerMap(Map<Long, Trainer> trainerMap) {
        this.trainerMap = trainerMap;
    }

    public void create(Trainer trainer) {
        trainerMap.put(trainer.getUserId(), trainer);
    }

    public void update(Trainer trainer) {
        trainerMap.put(trainer.getUserId(), trainer);
    }

    public Optional<Trainer> findByUsername(String username) {
        return trainerMap.values().stream()
                .filter(trainer -> username.equals(trainer.getUsername()))
                .findFirst();
    }

    public List<Trainer> listAll() {
        return new ArrayList<>(trainerMap.values());
    }
}