import PersistentClasses.User;
import PersistentClasses.Credentials;
import PersistentClasses.UserName;
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
        Credentials sashasCredentials = new Credentials(login, new UserName(firstName, lastName));
        User userSasha = new User(sashasCredentials);
        User userLena = new User(new Credentials("lena", new UserName("Lena", "Poliakova")));

        try {
            em.getTransaction().begin();

            em.persist(userSasha);
            em.persist(userLena);

            System.out.println(userSasha.getId());
            System.out.println(userLena.getId());

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

        assertEquals(login, foundUser.getCredentials().getLogin());
        assertEquals(firstName, foundUser.getCredentials().getName().getFirstName());
        assertEquals(lastName, foundUser.getCredentials().getName().getLastName());
    }
}
