package org.example.dao;

import org.example.models.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    void create(Trainee trainee);
    void update(Trainee trainee);
    void delete(Long userId);
    List<Trainee> listAll();
    Optional<Trainee> findByUsername(String username);

}
