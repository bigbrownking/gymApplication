package org.example.dao.impl;

import org.example.dao.TrainingDao;
import org.example.models.Trainee;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class TrainingDaoImpl implements TrainingDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public TrainingDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void create(Training training) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            TrainingTypeEntity trainingType = training.getTrainingType();
            TrainingTypeEntity existingTrainingType = session
                    .createQuery("FROM TrainingTypeEntity WHERE trainingTypeName = :name", TrainingTypeEntity.class)
                    .setParameter("name", trainingType.getTrainingTypeName())
                    .uniqueResult();

            if (existingTrainingType == null) {
                session.persist(trainingType);
            } else {
                training.setTrainingType(existingTrainingType);
            }
            session.persist(training);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateTraining(Training training) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(training);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Training> listAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Training> trainings = null;
        try {
            transaction = session.beginTransaction();
            trainings = session.createQuery("FROM Training", Training.class).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return trainings;
    }

    @Override
    public Training select(Long trainingId) {
        return null;
    }

    @Override
    public TrainingTypeEntity getTrainingType(String trainingType) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        TrainingTypeEntity trainingTypeEntity = null;
        try {
            transaction = session.beginTransaction();
            trainingTypeEntity = session.createQuery("FROM TrainingTypeEntity WHERE trainingTypeName =: trainingType", TrainingTypeEntity.class)
                    .setParameter("trainingType", trainingType)
                    .uniqueResult();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return trainingTypeEntity;
    }

    @Override
    public List<TrainingTypeEntity> getTrainingTypes() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;
        List<TrainingTypeEntity> trainingTypeEntities = null;
        try {
            transaction = session.beginTransaction();
            trainingTypeEntities = session.createQuery("FROM TrainingTypeEntity", TrainingTypeEntity.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();

        }

        return trainingTypeEntities;
    }
}
