package util;

import org.hibernate.Session;
import user.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class EntityManagerFactoryController {
    private final static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("myTest");
    private final static ThreadLocal<EntityManager> THREAD_LOCAL_ENTITY_MANAGER = new ThreadLocal<>();

    public static EntityManager getLocalEntityManager() {
        EntityManager em = THREAD_LOCAL_ENTITY_MANAGER.get();
        if (em == null || !em.isOpen()) {
            em = ENTITY_MANAGER_FACTORY.createEntityManager();
            THREAD_LOCAL_ENTITY_MANAGER.set(em);
        }
        return em;
    }

    public static void persist(Object... objects) {
        EntityManager em = getLocalEntityManager();
        try {
            em.getTransaction().begin();

            for (Object object : objects) {
                em.persist(object);
            }

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
        //TODO: check and log exceptions
    }

    public static void update(Object... objects) {
        EntityManager em = getLocalEntityManager();
        try {
            em.getTransaction().begin();

            for (Object object : objects) {
                em.merge(object);
            }

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
        //TODO: check and log exceptions
    }

    public static void delete(Object... objects) {
        EntityManager em = getLocalEntityManager();
        try {
            em.getTransaction().begin();

            for (Object object : objects) {
                em.unwrap(Session.class).saveOrUpdate(object);
                em.remove(object);
            }

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
        //TODO: check and log exceptions
    }

}
