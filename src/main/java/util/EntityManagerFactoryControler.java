package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryControler {
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
}
