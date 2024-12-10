package org.example.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.dao.TrainingDao;
import org.example.exceptions.InvalidDataException;
import org.example.metrics.DatabaseQueryMetrics;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import jakarta.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrainingDaoImpl implements TrainingDao {
    @PersistenceContext
    private final EntityManager entityManager;
    private final DatabaseQueryMetrics databaseQueryMetrics;

    @Override
    @Transactional
    public void create(Training training) throws InvalidDataException {
        try {
            databaseQueryMetrics.trackQueryDuration(() -> {
                try {
                    TrainingTypeEntity trainingType = training.getTrainingType();
                    TypedQuery<TrainingTypeEntity> query = entityManager.createQuery(
                            "FROM TrainingTypeEntity WHERE trainingTypeName = :name", TrainingTypeEntity.class);
                    query.setParameter("name", trainingType.getTrainingTypeName());
                    TrainingTypeEntity existingTrainingType = query.getResultList().stream().findFirst().orElse(null);

                    if (existingTrainingType == null) {
                        entityManager.persist(trainingType);
                    } else {
                        training.setTrainingType(existingTrainingType);
                    }
                    entityManager.persist(training);
                } catch (Exception e) {
                    throw new InvalidDataException("An error occurred while creating training: " + e.getMessage());
                }
                return null;
            });
        } catch (Exception e) {
            throw new InvalidDataException("An unexpected error occurred while creating training: " + e.getMessage());
        }
    }

    @Override
    public List<TrainingTypeEntity> getTrainingTypes() throws InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                try {
                    return entityManager.createQuery("FROM TrainingTypeEntity", TrainingTypeEntity.class).getResultList();
                } catch (Exception e) {
                    throw new InvalidDataException("An error occurred while retrieving training types: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            throw new InvalidDataException("An unexpected error occurred while retrieving training types: " + e.getMessage());
        }
    }
}
