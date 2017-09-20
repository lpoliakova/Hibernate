import PersistentClasses.Message;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Created by oradchykova on 7/8/17.
 */
public class MessageTest {
    @Test
    public void persistenceTest(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myTest");
        EntityManager em = emf.createEntityManager();

        Message message = new Message();
        message.setUserId(1L);
        message.setText("Concur the world");

        try {
            em.getTransaction().begin();

            em.persist(message);

            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        List<Message> messages;

        try {
            em.getTransaction().begin();

            messages = em.createQuery("select m from Message m", Message.class).getResultList();

            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        assertEquals(messages.size(), 1);
        assertEquals(messages.get(0).getUserId(), message.getUserId());
        assertEquals(messages.get(0).getText(), message.getText());
    }
}
