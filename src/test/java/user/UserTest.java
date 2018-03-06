package user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.SecuredUser;
import user.User;
import user.UserWithEmail;
import user.UserWithPhotos;
import user.Credentials;
import user.Image;
import user.UserName;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by oradchykova on 8/21/17.
 */
public class UserTest {
    private EntityManagerFactory emf;
    //private EntityManager em = emf.createEntityManager();

    @Test
    public void testUser(){
        String login = "sashen'ka";
        String firstName = "Oleksandr";
        String lastName = "Radchykov";
        int priority = 5;
        Credentials sashasCredentials = new Credentials(login, new UserName(firstName, lastName));
        User userSasha = new User(sashasCredentials);

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(userSasha);

            System.out.println(userSasha.getId());

            userSasha.setPriority(priority);

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }

        try {
            em = emf.createEntityManager();
            User foundUser = em.find(User.class, userSasha.getId());

            assertUserEquals(userSasha, foundUser);
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @Test
    public void testSecuredUser(){
        String login = "sashen'ka";
        String firstName = "Oleksandr";
        String lastName = "Radchykov";
        String password = "sasha";
        Credentials sashasCredentials = new Credentials(login, new UserName(firstName, lastName));
        User userSasha = new SecuredUser(sashasCredentials, password);

        persistUser(userSasha);

        EntityManager em = emf.createEntityManager();
        User foundUser = em.find(User.class, userSasha.getId());

        assertUserEquals(userSasha, foundUser);
        assertEquals(((SecuredUser)userSasha).getPassword(), ((SecuredUser)foundUser).getPassword());

        em.close();
    }

    @Test
    public void testUserWithEmail(){
        String login = "sashen'ka";
        String firstName = "Oleksandr";
        String lastName = "Radchykov";
        Set<String> emails = new HashSet<>();
        emails.add("sasha@exmp.com");
        emails.add("radchykov@exmp.com");
        Credentials sashasCredentials = new Credentials(login, new UserName(firstName, lastName));
        User userSasha = new UserWithEmail(sashasCredentials);
        emails.forEach(((UserWithEmail) userSasha)::addEmail);

        persistUser(userSasha);

        EntityManager em = emf.createEntityManager();
        User foundUser = em.find(User.class, userSasha.getId());

        assertUserEquals(userSasha, foundUser);
        assertEquals(((UserWithEmail)userSasha).getEmails(), ((UserWithEmail)foundUser).getEmails());

        em.close();
    }

    @Test
    public void testUserWithWrongEmail(){
        String login = "sashen'ka";
        String firstName = "Oleksandr";
        String lastName = "Radchykov";
        Set<String> emails = new HashSet<>();
        emails.add("radchykov.com");
        Credentials sashasCredentials = new Credentials(login, new UserName(firstName, lastName));
        User userSasha = new UserWithEmail(sashasCredentials);
        emails.forEach(((UserWithEmail) userSasha)::addEmail);

        try {
            persistUser(userSasha);
            assertFalse(true);
        } catch (javax.persistence.RollbackException ex) {
            assertTrue(true);
        }

        EntityManager em = emf.createEntityManager();
        User foundUser = em.find(User.class, userSasha.getId());

        assertNull(foundUser);
        em.close();
    }

    @Test
    public void testUserWithPhotos(){

        String login = "sashen'ka";
        String firstName = "Oleksandr";
        String lastName = "Radchykov";
        Map<String, Image> photos = new HashMap<>();
        photos.put("myPass", new Image("photos/passport", ".png", 300, 400));
        photos.put("Italy Beach", new Image("photos/italy/beach", ".jpg"));
        Credentials sashasCredentials = new Credentials(login, new UserName(firstName, lastName));
        User userSasha = new UserWithPhotos(sashasCredentials);
        photos.forEach(((UserWithPhotos) userSasha)::addPhoto);

        persistUser(userSasha);

        EntityManager em = emf.createEntityManager();
        User foundUser = em.find(User.class, userSasha.getId());

        assertUserEquals(userSasha, foundUser);
        assertEquals(((UserWithPhotos)userSasha).getPhotos(), ((UserWithPhotos)foundUser).getPhotos());

        em.close();
    }

    private void persistUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(user);

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    private void assertUserEquals(User first, User second) {
        assertEquals(first.getClass(), second.getClass());
        assertEquals(first.getCredentials(), second.getCredentials());
        assertEquals(first.getCredentials().getName(), second.getCredentials().getName());
        assertEquals(first.getGroups(), second.getGroups());
        assertEquals(first.getDevice(), second.getDevice());
        assertEquals(first.getPriority(), second.getPriority());
    }

    @Before
    public void initEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("myTest");
    }

    @After
    public void dropEntityManagerFactory() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
