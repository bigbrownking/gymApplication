package org.example.dao.impl;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
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
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDaoImpl implements TrainerDao {
    private final SessionFactory sessionFactory;
    private final DatabaseQueryMetrics databaseQueryMetrics;

    @Autowired
    public TrainerDaoImpl(EntityManagerFactory entityManagerFactory,
                          DatabaseQueryMetrics databaseQueryMetrics) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        this.databaseQueryMetrics = databaseQueryMetrics;
    }

    @Override
    public void create(Trainer trainer) throws InvalidDataException{
        try {
            databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.getCurrentSession();
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    session.persist(trainer);
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } finally {
                    session.close();
                }
            });
        }catch (Exception e){
            throw new InvalidDataException("An error occurred, while creating trainer: " + e.getMessage());
        }
    }

    @Override
    public void update(Trainer trainer) throws EntityNotFoundException, InvalidDataException{
        databaseQueryMetrics.trackQueryDuration(() -> {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Trainer existingTrainer = session.get(Trainer.class, trainer.getTrainerId());
                if (existingTrainer == null) {
                    throw new EntityNotFoundException("Trainer with ID " + trainer.getTrainerId() + " not found");
                }
                if (!isValidTrainer(trainer)) {
                    throw new InvalidDataException("Trainer data is invalid");
                }
                session.merge(trainer);
                transaction.commit();
            } catch (InvalidDataException | EntityNotFoundException e) {
                if (transaction != null) transaction.rollback();
                throw e;
            }
            catch (Exception e) {
                if (transaction != null) transaction.rollback();
                throw new InvalidDataException("An unexpected error occurred: " + e.getMessage());
            } finally {
                session.close();
            }
        });
    }


    private boolean isValidTrainer(Trainer trainer){
        return trainer.getTrainerId() != null &&
                trainer.getPassword() != null &&
                trainer.getUsername() != null &&
                isSpecializationValid(trainer.getSpecialization());
    }
    @Override
    public List<Trainer> listAll() throws InvalidDataException{
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.getCurrentSession();
                Transaction transaction = null;
                List<Trainer> trainers;
                try {
                    transaction = session.beginTransaction();
                    trainers = session.createQuery("FROM Trainer", Trainer.class).list();
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw new InvalidDataException("Error listing trainers: " + e.getMessage());
                } finally {
                    session.close();
                }
                return trainers;
            });
        } catch (Exception e){
            throw new InvalidDataException("An error occurred while retrieving the trainer list: " + e.getMessage());
        }
    }

    @Override
    public Optional<Trainer> findByUsername(String username) throws EntityNotFoundException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.getCurrentSession();
                Transaction transaction = null;
                Optional<Trainer> trainer;
                try {
                    transaction = session.beginTransaction();
                    trainer = session.createQuery("FROM Trainer WHERE username = :username", Trainer.class).
                            setParameter("username", username)
                            .uniqueResultOptional();
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw new InvalidDataException("Error finding trainer by username: " + e.getMessage());
                } finally {
                    session.close();
                }
                return trainer;
            });
        } catch (Exception e){
            throw new EntityNotFoundException("Trainer not found for username: " + username);
        }
    }

    @Override
    public List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String traineeName) throws InvalidDataException{
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.getCurrentSession();
                Transaction transaction = null;
                List<Training> trainings;
                try {
                    transaction = session.beginTransaction();
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
                    TypedQuery<Training> finalQuery = session.createQuery(query.toString());
                    finalQuery.setParameter("username", username);

                    if (startDate != null) {
                        finalQuery.setParameter("startDate", startDate);
                    }
                    if (endDate != null) {
                        finalQuery.setParameter("endDate", endDate);
                    }
                    if (traineeName != null && !traineeName.isEmpty()) {
                        finalQuery.setParameter("traineeName", traineeName);
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
    public boolean isSpecializationValid(TrainingTypeEntity trainingTypeEntity) {

        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            TrainingTypeEntity specialization = trainingTypeEntity;
            TrainingTypeEntity existingSpecialization = session
                    .createQuery("FROM TrainingTypeEntity WHERE trainingTypeName = :name", TrainingTypeEntity.class)
                    .setParameter("name", specialization.getTrainingTypeName())
                    .uniqueResult();

            if (existingSpecialization == null) {
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        return true;
    }

    @Override
    public List<Trainee> allTraineesOfTrainer(String username) throws EntityNotFoundException {
        try {
            return databaseQueryMetrics.trackQueryDuration(() -> {
                Session session = sessionFactory.getCurrentSession();
                Transaction transaction = null;
                List<Trainee> trainees;
                try {
                    transaction = session.beginTransaction();
                    trainees = session.createQuery(
                                    "SELECT t FROM Trainee t JOIN t.trainings tr WHERE tr.trainer.username = :username", Trainee.class)
                            .setParameter("username", username)
                            .getResultList();
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    throw new InvalidDataException("Error retrieving trainees for trainer: " + e.getMessage());
                } finally {
                    session.close();
                }
                return trainees;
            });
        } catch (Exception e){
            throw new EntityNotFoundException("Trainees not found for trainer with username: " + username);
        }
    }

}
