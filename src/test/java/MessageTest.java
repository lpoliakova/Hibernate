import entities.Message;
import entities.User;
import org.junit.Test;
import values.Credentials;
import values.UserName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

        String login = "sashenka";
        String firstName = "Oleksandr";
        String lastName = "Radchykov";
        User userSasha = new User(new Credentials(login, new UserName(firstName, lastName)));
        Message message;

        try {
            em.getTransaction().begin();

            message = userSasha.writeMessage("Concur the world");
            em.persist(userSasha);

            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        User actualUser = emf.createEntityManager().find(User.class, userSasha.getId());
        List<Message> actualMessages = actualUser.getMessages();

        assertNotNull(actualMessages);
        assertEquals(1, actualMessages.size());

        Message actualMessage = actualMessages.get(0);

        assertEquals(message.getCreator().getId(), actualMessage.getCreator().getId());
        assertEquals(message.getText(), actualMessage.getText());

        System.out.println(actualMessage.getCreationTimestamp());
    }
}
