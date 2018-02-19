import entities.*;
import org.junit.Test;
import values.Credentials;
import values.Image;
import values.UserName;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by oradchykova on 8/21/17.
 */
public class UserTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("myTest");
    private EntityManager em = emf.createEntityManager();

    @Test
    public void testUser(){
        String login = "sashen'ka";
        String firstName = "Oleksandr";
        String lastName = "Radchykov";
        int priority = 5;
        Credentials sashasCredentials = new Credentials(login, new UserName(firstName, lastName));
        User userSasha = new User(sashasCredentials);

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
        }

        User foundUser = em.find(User.class, userSasha.getId());

        assertUserEquals(userSasha, foundUser);
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

        User foundUser = em.find(User.class, userSasha.getId());

        assertUserEquals(userSasha, foundUser);
        assertEquals(((SecuredUser)userSasha).getPassword(), ((SecuredUser)foundUser).getPassword());
    }

    @Test
    public void testUserWithEmail(){
        String login = "sashen'ka";
        String firstName = "Oleksandr";
        String lastName = "Radchykov";
        Set<String> emails = new HashSet<>();
        emails.add("sasha@ex.com");
        emails.add("radchykov@ex.com");
        Credentials sashasCredentials = new Credentials(login, new UserName(firstName, lastName));
        User userSasha = new UserWithEmail(sashasCredentials);
        emails.forEach(((UserWithEmail) userSasha)::addEmail);

        persistUser(userSasha);

        User foundUser = em.find(User.class, userSasha.getId());

        assertUserEquals(userSasha, foundUser);
        assertEquals(((UserWithEmail)userSasha).getEmails(), ((UserWithEmail)foundUser).getEmails());
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

        User foundUser = em.find(User.class, userSasha.getId());

        assertUserEquals(userSasha, foundUser);
        assertEquals(((UserWithPhotos)userSasha).getPhotos(), ((UserWithPhotos)foundUser).getPhotos());
    }

    private void persistUser(User user) {
        try {
            em.getTransaction().begin();

            em.persist(user);

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
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
}
