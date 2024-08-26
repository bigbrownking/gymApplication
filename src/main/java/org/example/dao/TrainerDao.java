package org.example.dao;

import org.example.models.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    void create(Trainer trainer);
    void update(Trainer trainer);
    Optional<Trainer> findByUsername(String username);
    List<Trainer> listAll();
}
