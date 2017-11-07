import PersistentClasses.*;
import org.junit.Test;

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
        Long sashaId;

        Message message = new Message();
        message.setText("Concur the world");

        try {
            em.getTransaction().begin();

            em.persist(userSasha);
            sashaId = userSasha.getId();
            message.setUserId(sashaId);
            em.persist(message);

            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        UserMessageInfo userMessageInfo;

        try {
            em.getTransaction().begin();

            userMessageInfo = em.find(UserMessageInfo.class, sashaId);

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
