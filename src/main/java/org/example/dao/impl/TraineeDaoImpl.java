package org.example.dao.impl;


import jakarta.persistence.TypedQuery;
import org.example.dao.TraineeDao;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TraineeDaoImpl implements TraineeDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public TraineeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Trainee trainee) {
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
    }

    @Override
    public void update(Trainee trainee) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(trainee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String username) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        boolean isDeleted = false;
        try {
            transaction = session.beginTransaction();
            Long traineeId = (Long) session.createQuery("SELECT t.id FROM Trainee t WHERE t.username = :username")
                    .setParameter("username", username)
                    .uniqueResult();

            if (traineeId != null) {
                session.createQuery("DELETE FROM Training t WHERE t.trainee.id = :traineeId")
                        .setParameter("traineeId", traineeId)
                        .executeUpdate();

                int result = session.createQuery("DELETE FROM Trainee t WHERE t.username = :username")
                        .setParameter("username", username)
                        .executeUpdate();

                isDeleted = result > 0;
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return isDeleted;
    }

    @Override
    public List<Trainee> listAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Trainee> trainees = null;
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
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Optional<Trainee> trainee = null;
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
    }

    @Override
    public Optional<Trainee> findByUsernameAndPassword(String username, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Optional<Trainee> trainee = null;
        try {
            transaction = session.beginTransaction();
            trainee = session.createQuery("FROM Trainee WHERE username = :username AND password = :password", Trainee.class).
                    setParameter("username", username).
                    setParameter("password", password).
                    uniqueResultOptional();
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
    }

    @Override
    public List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String trainerName, TrainingTypeEntity trainingType) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Training> trainings = null;
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
            throw e;
        } finally {
            session.close();
        }
        return trainings;
    }

    @Override
    public List<Trainer> getTrainersNotAssignedToTrainee(String username) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Trainer> trainersNotAssigned = null;
        try {
            transaction = session.beginTransaction();
            String assignedTrainersQuery = "SELECT DISTINCT t.trainer FROM Training t " +
                    "JOIN t.trainee trainee WHERE trainee.username = :username";
            Query<Trainer> assignedTrainersQueryObj = session.createQuery(assignedTrainersQuery, Trainer.class);
            assignedTrainersQueryObj.setParameter("username", username);
            List<Trainer> assignedTrainers = assignedTrainersQueryObj.getResultList();

            String allTrainersQuery = "FROM Trainer";
            Query<Trainer> allTrainersQueryObj = session.createQuery(allTrainersQuery, Trainer.class);
            List<Trainer> allTrainers = allTrainersQueryObj.getResultList();

            trainersNotAssigned = allTrainers.stream()
                    .filter(trainer -> !assignedTrainers.contains(trainer))
                    .toList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return trainersNotAssigned;
    }

    @Override
    public List<Trainer> getTrainersAssignedToTrainee(String username) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Trainer> assignedTrainers = null;
        try {
            transaction = session.beginTransaction();
            String hql = "SELECT DISTINCT t.trainer FROM Training t " +
                    "JOIN t.trainee trainee WHERE trainee.username = :username";
            Query<Trainer> query = session.createQuery(hql, Trainer.class);
            query.setParameter("username", username);
            assignedTrainers = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
        return assignedTrainers;
    }
}
