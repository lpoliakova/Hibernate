import entities.*;
import org.junit.Test;
import values.Credentials;
import values.UserName;

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
        int priority = 5;
        Credentials sashasCredentials = new Credentials(login, new UserName(firstName, lastName));
        User userSasha = new SecuredUser(sashasCredentials, "sasha");
        User userLena = new User(new Credentials("lena", new UserName("Lena", "Poliakova")));
        User userLera = new UserWithEmail(new Credentials("lera", new UserName("Lera", "Poliakova")), "lera@ex.com");

        try {
            em.getTransaction().begin();

            em.persist(userSasha);
            em.persist(userLena);
            em.persist(userLera);

            System.out.println(userSasha.getId());
            System.out.println(userLena.getId());
            System.out.println(userLera.getId());

            userSasha.setPriority(priority);

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

        assertEquals(SecuredUser.class, foundUser.getClass());

        assertEquals(login, foundUser.getCredentials().getLogin());
        assertEquals(firstName, foundUser.getCredentials().getName().getFirstName());
        assertEquals(lastName, foundUser.getCredentials().getName().getLastName());
        assertEquals(priority, foundUser.getPriority());
    }
}
