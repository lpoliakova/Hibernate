import PersistentClasses.User;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

/**
 * Created by oradchykova on 8/21/17.
 */
public class UserTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("myTest");

    @Test
    public void testUser(){
        EntityManager em = emf.createEntityManager();

        String login = "sashen'ka";
        String firstName = "Oleksandr";
        String lastName = "Radchykov";
        User userSasha = new User(login, firstName, lastName);

        try {
            em.getTransaction().begin();

            em.persist(userSasha);

            System.out.println(userSasha.getId());

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }

        User foundUser;

        try {
            em.getTransaction().begin();

            foundUser = em.find(User.class, userSasha.getId());

            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        assertEquals(login, foundUser.getLogin());
        assertEquals(firstName, foundUser.getFirstName());
        assertEquals(lastName, foundUser.getLastName());
    }
}
