package org.example.dao.impl;

import jakarta.persistence.TypedQuery;
import org.example.dao.TrainerDao;
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

    @Autowired
    public TrainerDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean create(Trainer trainer) {
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
            throw e;
        } finally {
            session.close();
        }
        return true;
    }

    @Override
    public void update(Trainer trainer) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(trainer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Trainer> listAll() {
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
            throw e;
        } finally {
            session.close();
        }
        return trainers;
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        Optional<Trainer> trainer = null;
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
            throw e;
        } finally {
            session.close();
        }
        return trainer;
    }
    @Override
    public Optional<Trainer> findByUsernameAndPassword(String username, String password) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        Optional<Trainer> trainer = null;
        try {
            transaction = session.beginTransaction();
            trainer = session.createQuery("FROM Trainer WHERE username = :username AND password = :password", Trainer.class).
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
        return trainer;
    }

    @Override
    public List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String traineeName) {
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
            throw e;
        } finally {
            session.close();
        }
        return trainings;
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
        } catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        return true;
    }

    @Override
    public List<Trainee> allTraineesOfTrainer(String username) {
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
            throw e;
        } finally {
            session.close();
        }
        return trainees;
    }

}
