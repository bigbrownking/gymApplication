package org.example.dao.impl;


import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
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

import java.util.*;

@Repository
public class TraineeDaoImpl implements TraineeDao {
    private final SessionFactory sessionFactory;
    private final DatabaseQueryMetrics databaseQueryMetrics;

    @Autowired
    public TraineeDaoImpl(EntityManagerFactory entityManagerFactory,
                          DatabaseQueryMetrics databaseQueryMetrics) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        this.databaseQueryMetrics = databaseQueryMetrics;
    }

    @Override
    public void create(Trainee trainee) throws InvalidDataException {
        try {
            databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.getCurrentSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    session.persist(trainee);
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw e;
                } finally {
                    session.close();
                }
            });
        }catch (Exception e){
            throw  new InvalidDataException("An error occurred, while creating trainee: "+ e.getMessage());
        }
    }

    @Override
    public void update(Trainee trainee) throws EntityNotFoundException, InvalidDataException {
        databaseQueryMetrics.trackQueryDuration(() -> {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Trainee existingTrainee = session.get(Trainee.class, trainee.getTraineeId());
                if(existingTrainee == null){
                    throw new EntityNotFoundException("Trainee with ID " + trainee.getTraineeId() + " not found");
                }
                if(!isValidTrainee(trainee)){
                    throw new InvalidDataException("Trainee data is invalid");
                }
                session.merge(trainee);
                transaction.commit();
            } catch (InvalidDataException | EntityNotFoundException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw e;
            }  catch (Exception e) {
                if (transaction != null) transaction.rollback();
                throw new InvalidDataException("An unexpected error occurred: " + e.getMessage());
            }finally {
                session.close();
            }
        });
    }

    private boolean isValidTrainee(Trainee trainee){
        return trainee.getTraineeId() != null &&
                trainee.getPassword() != null &&
                trainee.getUsername() != null;
    }
    @Override
    public boolean delete(String username) throws EntityNotFoundException, InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                Transaction transaction = null;
                try (Session session = sessionFactory.getCurrentSession()) {
                    transaction = session.beginTransaction();

                    Long traineeId = (Long) session.createQuery("SELECT t.id FROM Trainee t WHERE t.username = :username")
                            .setParameter("username", username)
                            .uniqueResult();

                    if (traineeId == null) {
                        throw new EntityNotFoundException("Trainee not found with username: " + username);
                    }

                    session.createQuery("DELETE FROM Training t WHERE t.trainee.id = :traineeId")
                            .setParameter("traineeId", traineeId)
                            .executeUpdate();

                    int result = session.createQuery("DELETE FROM Trainee t WHERE t.username = :username")
                            .setParameter("username", username)
                            .executeUpdate();

                    transaction.commit();
                    return result > 0;

                } catch (Exception e) {
                    if (transaction != null && transaction.getStatus().canRollback()) {
                        transaction.rollback();
                    }
                    throw new EntityNotFoundException("Error occurred while deleting trainee: " + e.getMessage());
                }
            });
        } catch (Exception e){
            throw new InvalidDataException("Error occurred while deleting trainee: " + e.getMessage());
        }
    }


    @Override
    public List<Trainee> listAll() throws InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.openSession();
                Transaction transaction = null;
                List<Trainee> trainees;
                try {
                    transaction = session.beginTransaction();
                    trainees = session.createQuery("FROM Trainee", Trainee.class).list();
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw e;
                } finally {
                    session.close();
                }
                return trainees;
            });
        } catch (Exception e){
            throw new InvalidDataException("An error occurred while retrieving the trainee list: " + e.getMessage());
        }
    }

    @Override
    public Optional<Trainee> findByUsername(String username) throws EntityNotFoundException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.openSession();
                Transaction transaction = null;
                Optional<Trainee> trainee;
                try {
                    transaction = session.beginTransaction();
                    trainee = session.createQuery("FROM Trainee WHERE username = :username", Trainee.class).
                            setParameter("username", username)
                            .uniqueResultOptional();
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw e;
                } finally {
                    session.close();
                }
                return trainee;
            });
        }catch (Exception e){
            throw new EntityNotFoundException("Trainee not found for username: " + username);
        }
    }


    @Override
    public List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String
            trainerName, TrainingTypeEntity trainingType) throws InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.openSession();
                Transaction transaction = null;
                List<Training> trainings;
                try {
                    transaction = session.beginTransaction();
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
                    TypedQuery<Training> finalQuery = session.createQuery(query.toString());
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
                    trainings = finalQuery.getResultList();
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw new InvalidDataException("Error retrieving trainings: " + e.getMessage());
                } finally {
                    session.close();
                }
                return trainings;
            });
        } catch (Exception e){
            throw new InvalidDataException("An error occurred while retrieving trainings: " + e.getMessage());
        }
    }

    @Override
    public List<Trainer> getTrainersNotAssignedToTrainee(String username) throws EntityNotFoundException, InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.openSession();
                Transaction transaction = null;
                List<Trainer> trainersNotAssigned;
                try {
                    transaction = session.beginTransaction();
                    List<Trainer> assignedTrainers = getAssignedTrainersByUsername(session, username);
                    List<Trainer> allTrainers = session.createQuery("FROM Trainer", Trainer.class).getResultList();
                    trainersNotAssigned = allTrainers.stream()
                            .filter(trainer -> !assignedTrainers.contains(trainer))
                            .toList();

                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw new EntityNotFoundException("Error fetching trainers not assigned to trainee: " + username);
                } finally {
                    session.close();
                }
                return trainersNotAssigned;
            });
        } catch (Exception e){
            throw new InvalidDataException("An error occurred while retrieving trainers: " + e.getMessage());
        }
    }

    @Override
    public List<Trainer> getTrainersAssignedToTrainee(String username) throws EntityNotFoundException, InvalidDataException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.openSession();
                Transaction transaction = null;
                List<Trainer> assignedTrainers;
                try {
                    transaction = session.beginTransaction();

                    assignedTrainers = getAssignedTrainersByUsername(session, username);

                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw new EntityNotFoundException("Error fetching trainers assigned to trainee: " + username);
                } finally {
                    session.close();
                }
                return assignedTrainers;
            });
        } catch (Exception e){
            throw new InvalidDataException("An error occurred while retrieving trainers: " + e.getMessage());
        }
    }

    private List<Trainer> getAssignedTrainersByUsername(Session session, String username) {
        String hql = "SELECT DISTINCT t.trainer FROM Training t JOIN t.trainee trainee WHERE trainee.username = :username";
        return session.createQuery(hql, Trainer.class)
                .setParameter("username", username)
                .getResultList();
    }

}
