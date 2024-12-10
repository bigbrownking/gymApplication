package org.example.dao.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.example.dao.TraineeDao;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.InvalidDataException;
import org.example.metrics.DatabaseQueryMetrics;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class TraineeDaoImpl implements TraineeDao {
    @PersistenceContext
    private final EntityManager entityManager;
    private final DatabaseQueryMetrics databaseQueryMetrics;


    @Transactional
    @Override
    public void create(Trainee trainee) throws InvalidDataException {
        try {
            databaseQueryMetrics.trackQueryDuration(() -> {
                entityManager.persist(trainee);
            });
        } catch (Exception e) {
            throw new InvalidDataException("An error occurred while creating trainee: " + e.getMessage());
        }
    }


    @Transactional
    @Override
    public void update(Trainee trainee) throws EntityNotFoundException, InvalidDataException {
        databaseQueryMetrics.trackQueryDuration(() -> {
            Trainee existingTrainee = entityManager.find(Trainee.class, trainee.getTraineeId());
            if (existingTrainee == null) {
                throw new EntityNotFoundException("Trainee with ID " + trainee.getTraineeId() + " not found");
            }
            if (!isValidTrainee(trainee)) {
                throw new InvalidDataException("Trainee data is invalid");
            }
            entityManager.merge(trainee);
        });
    }

    private boolean isValidTrainee(Trainee trainee){
        return trainee.getTraineeId() != null &&
                trainee.getPassword() != null &&
                trainee.getUsername() != null;
    }
    @Transactional
    @Override
    public boolean delete(String username) throws EntityNotFoundException, InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                TypedQuery<Long> query = entityManager.createQuery(
                        "SELECT t.id FROM Trainee t WHERE t.username = :username", Long.class);
                query.setParameter("username", username);
                Long traineeId = query.getSingleResult();

                if (traineeId == null) {
                    throw new EntityNotFoundException("Trainee not found with username: " + username);
                }
                //Set null to trainings that are gone
                entityManager.createQuery(
                                "UPDATE Training t SET t.trainee = null WHERE t.trainee.id = :traineeId AND t.trainingDate < :currentDate")
                        .setParameter("traineeId", traineeId)
                        .setParameter("currentDate", java.time.LocalDateTime.now())
                        .executeUpdate();

                // Delete trainings that are in future
                entityManager.createQuery(
                                "DELETE FROM Training t WHERE t.trainee.id = :traineeId AND t.trainingDate > :currentDate")
                        .setParameter("traineeId", traineeId)
                        .setParameter("currentDate", java.time.LocalDateTime.now())
                        .executeUpdate();

                int result = entityManager.createQuery("DELETE FROM Trainee t WHERE t.username = :username")
                        .setParameter("username", username)
                        .executeUpdate();


                return result > 0;
            });
        } catch (Exception e) {
            throw new InvalidDataException("Error occurred while deleting trainee: " + e.getMessage());
        }
    }



    @Override
    public List<Trainee> listAll() throws InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                TypedQuery<Trainee> query = entityManager.createQuery("FROM Trainee", Trainee.class);
                return query.getResultList();
            });
        } catch (Exception e) {
            throw new InvalidDataException("An error occurred while retrieving the trainee list: " + e.getMessage());
        }
    }

    @Override
    public Optional<Trainee> findByUsername(String username) throws EntityNotFoundException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                TypedQuery<Trainee> query = entityManager.createQuery(
                        "FROM Trainee WHERE username = :username", Trainee.class);
                query.setParameter("username", username);
                return query.getResultList().stream().findFirst();
            });
        } catch (Exception e) {
            throw new EntityNotFoundException("Trainee not found for username: " + username);
        }
    }

    @Override
    public List<Training> getTrainingByCriteria(String username, LocalDateTime startDate, LocalDateTime endDate, String trainerName, TrainingTypeEntity trainingType) throws InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                StringBuilder query = new StringBuilder("FROM Training WHERE trainee.username =:username");

                if (startDate != null) {
                    query.append(" AND trainingDate >= :startDate");
                }
                if (endDate != null) {
                    query.append(" AND trainingDate <= :endDate");
                }
                if (trainerName != null && !trainerName.isEmpty()) {
                    query.append(" AND trainer.username LIKE :trainerName");
                }
                if (trainingType != null) {
                    query.append(" AND trainingType = :trainingType");
                }

                TypedQuery<Training> finalQuery = entityManager.createQuery(query.toString(), Training.class);
                finalQuery.setParameter("username", username);

                if (startDate != null) {
                    finalQuery.setParameter("startDate", startDate);
                }
                if (endDate != null) {
                    finalQuery.setParameter("endDate", endDate);
                }
                if (trainerName != null && !trainerName.isEmpty()) {
                    finalQuery.setParameter("trainerName", trainerName);
                }
                if (trainingType != null) {
                    finalQuery.setParameter("trainingType", trainingType);
                }

                return finalQuery.getResultList();
            });
        } catch (Exception e) {
            throw new InvalidDataException("Error retrieving trainings: " + e.getMessage());
        }
    }

    @Override
    public List<Trainer> getTrainersNotAssignedToTrainee(String username) throws EntityNotFoundException, InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                TypedQuery<Trainer> assignedTrainersQuery = entityManager.createQuery(
                        "SELECT DISTINCT t.trainer FROM Training t JOIN t.trainee trainee WHERE trainee.username = :username", Trainer.class);
                assignedTrainersQuery.setParameter("username", username);
                List<Trainer> assignedTrainers = assignedTrainersQuery.getResultList();

                TypedQuery<Trainer> allTrainersQuery = entityManager.createQuery("FROM Trainer", Trainer.class);
                List<Trainer> allTrainers = allTrainersQuery.getResultList();

                return allTrainers.stream()
                        .filter(trainer -> !assignedTrainers.contains(trainer))
                        .toList();
            });
        } catch (Exception e) {
            throw new InvalidDataException("Error occurred while retrieving trainers not assigned to trainee: " + username);
        }
    }
    @Override
    public List<Trainer> getTrainersAssignedToTrainee(String username) throws EntityNotFoundException, InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                TypedQuery<Trainer> query = entityManager.createQuery(
                        "SELECT DISTINCT t.trainer FROM Training t JOIN t.trainee trainee WHERE trainee.username = :username", Trainer.class);
                query.setParameter("username", username);
                return query.getResultList();
            });
        } catch (Exception e) {
            throw new InvalidDataException("Error occurred while retrieving trainers assigned to trainee: " + username);
        }
    }
}
