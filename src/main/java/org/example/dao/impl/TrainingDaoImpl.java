package org.example.dao.impl;

import jakarta.persistence.EntityManagerFactory;
import org.example.dao.TrainingDao;
import org.example.metrics.DatabaseQueryMetrics;
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
    private final DatabaseQueryMetrics databaseQueryMetrics;

    @Autowired
    public TrainingDaoImpl(EntityManagerFactory entityManagerFactory, DatabaseQueryMetrics databaseQueryMetrics) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        this.databaseQueryMetrics = databaseQueryMetrics;
    }

    @Override
    @Transactional
    public void create(Training training) {
        databaseQueryMetrics.trackQueryDuration(() -> {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                TrainingTypeEntity trainingType = training.getTrainingType();
                TrainingTypeEntity existingTrainingType = session.createQuery("FROM TrainingTypeEntity WHERE trainingTypeName = :name", TrainingTypeEntity.class).setParameter("name", trainingType.getTrainingTypeName()).uniqueResult();

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
        });
    }

    @Override
    public List<TrainingTypeEntity> getTrainingTypes() throws Exception {
        return databaseQueryMetrics.trackQueryDuration(() -> {
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
        });
    }
}
