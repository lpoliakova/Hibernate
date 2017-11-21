import entities.*;
import org.junit.Test;
import util.UserMessageInfo;
import values.Credentials;
import values.UserName;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;

public class UserMessageInfoTest {
    @Test
    public void persistenceTest(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myTest");
        EntityManager em = emf.createEntityManager();

        String login = "sashenka";
        String firstName = "Oleksandr";
        String lastName = "Radchykov";
        User userSasha = new User(new Credentials(login, new UserName(firstName, lastName)));
        userSasha.writeMessage("Concur the world");

        try {
            em.getTransaction().begin();

            em.persist(userSasha);

            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        UserMessageInfo userMessageInfo;

        try {
            em.getTransaction().begin();

            userMessageInfo = em.find(UserMessageInfo.class, userSasha.getId());

            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        assertEquals(login, userMessageInfo.getUserLogin());
        assertEquals(new Integer(1), userMessageInfo.getMessageCount());
    }
}
