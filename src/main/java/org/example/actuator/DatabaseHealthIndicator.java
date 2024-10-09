package org.example.actuator;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final SessionFactory sessionFactory;

    @Autowired
    public DatabaseHealthIndicator(EntityManagerFactory entityManagerFactory) {
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Override
    public Health health() {
        try(Session session = sessionFactory.openSession()){
            session.createNativeQuery("SELECT 1").getSingleResult();
            return Health.up().withDetail("database", "PostgreSQL is running").build();
        } catch (Exception e){
            return Health.down(e).withDetail("database", "PostgreSQL is not reachable").build();
        }
    }
}
