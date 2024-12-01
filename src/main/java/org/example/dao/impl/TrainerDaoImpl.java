package org.example.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.example.dao.TrainerDao;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.InvalidDataException;
import org.example.metrics.DatabaseQueryMetrics;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class TrainerDaoImpl implements TrainerDao {
    @PersistenceContext
    private final EntityManager entityManager;
    private final DatabaseQueryMetrics databaseQueryMetrics;

    @Override
    public void create(Trainer trainer) throws InvalidDataException {
        try {
            databaseQueryMetrics.trackQueryDuration(() -> {
                entityManager.persist(trainer);  // Simplified; Spring will handle transaction
            });
        } catch (Exception e) {
            throw new InvalidDataException("An error occurred while creating trainer: " + e.getMessage());
        }
    }

    @Override
    public void update(Trainer trainer) throws EntityNotFoundException, InvalidDataException {
        databaseQueryMetrics.trackQueryDuration(() -> {
            Trainer existingTrainer =entityManager.find(Trainer.class, trainer.getTrainerId());
            if (existingTrainer == null) {
                throw new EntityNotFoundException("Trainer with ID " + trainer.getTrainerId() + " not found");
            }
            if (!isValidTrainer(trainer)) {
                throw new InvalidDataException("Trainer data is invalid");
            }
            entityManager.merge(trainer);
        });
    }

    private boolean isValidTrainer(Trainer trainer){
        return trainer.getTrainerId() != null &&
                trainer.getPassword() != null &&
                trainer.getUsername() != null &&
                isSpecializationValid(trainer.getSpecialization());
    }
    @Override
    public List<Trainer> listAll() throws InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                TypedQuery<Trainer> query = entityManager.createQuery("FROM Trainer", Trainer.class);
                return query.getResultList();
            });
        } catch (Exception e) {
            throw new InvalidDataException("An error occurred while retrieving the trainer list: " + e.getMessage());
        }
    }

    @Override
    public Optional<Trainer> findByUsername(String username) throws EntityNotFoundException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                TypedQuery<Trainer> query = entityManager.createQuery("FROM Trainer WHERE username = :username", Trainer.class);
                query.setParameter("username", username);
                return query.getResultList().stream().findFirst();  // Transaction managed by Spring
            });
        } catch (Exception e) {
            throw new EntityNotFoundException("Trainer not found for username: " + username);
        }
    }

    @Override
    public List<Training> getTrainingByCriteria(String username, LocalDateTime startDate, LocalDateTime endDate, String traineeName) throws InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                StringBuilder query = new StringBuilder("FROM Training WHERE trainer.username =:username");

                if (startDate != null) {
                    query.append(" AND trainingDate >= :startDate");
                }
                if (endDate != null) {
                    query.append(" AND trainingDate <= :endDate");
                }
                if (traineeName != null && !traineeName.isEmpty()) {
                    query.append(" AND trainee.username LIKE :traineeName");
                }
                TypedQuery<Training> finalQuery = entityManager.createQuery(query.toString(), Training.class);                finalQuery.setParameter("username", username);
                if (startDate != null) {
                    finalQuery.setParameter("startDate", startDate);
                }
                if (endDate != null) {
                    finalQuery.setParameter("endDate", endDate);
                }
                if (traineeName != null && !traineeName.isEmpty()) {
                    finalQuery.setParameter("traineeName", traineeName);
                }
                return finalQuery.getResultList();  // Transaction managed by Spring
            });
        } catch (Exception e) {
            throw new InvalidDataException("An error occurred while retrieving trainings: " + e.getMessage());
        }
    }

    @Override
    public boolean isSpecializationValid(TrainingTypeEntity trainingTypeEntity) {
        TrainingTypeEntity existingSpecialization = entityManager
                .createQuery("FROM TrainingTypeEntity WHERE trainingTypeName = :name", TrainingTypeEntity.class)
                .setParameter("name", trainingTypeEntity.getTrainingTypeName())
                .getSingleResult();


        return existingSpecialization != null;
    }

    @Override
    public List<Trainee> allTraineesOfTrainer(String username) throws EntityNotFoundException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                TypedQuery<Trainee> query = entityManager.createQuery(
                        "SELECT t FROM Trainee t JOIN t.trainings tr WHERE tr.trainer.username = :username", Trainee.class);
                query.setParameter("username", username);
                return query.getResultList();
            });
            } catch (Exception e) {
            throw new EntityNotFoundException("Trainees not found for trainer with username: " + username);
        }
    }
}
