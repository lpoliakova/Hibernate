import entities.Group;
import entities.Message;
import entities.User;
import entities.UserInGroup;
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
        Group world = new Group("world");
        User userSasha = new User(new Credentials(login, new UserName(firstName, lastName)));
        UserInGroup sashaInWorld;
        Message message;

        try {
            em.getTransaction().begin();

            em.persist(userSasha);
            em.persist(world);
            sashaInWorld = new UserInGroup(world, userSasha);
            message = sashaInWorld.writeMessage("Concur the world");
            em.persist(sashaInWorld);

            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        UserInGroup actualPlace = emf.createEntityManager().find(UserInGroup.class, sashaInWorld.getId());
        List<Message> actualMessages = actualPlace.getMessages();

        assertNotNull(actualMessages);
        assertEquals(1, actualMessages.size());

        Message actualMessage = actualMessages.get(0);

        assertEquals(message.getPlace().getId(), actualMessage.getPlace().getId());
        assertEquals(message.getText(), actualMessage.getText());

        System.out.println(actualMessage.getCreationTimestamp());
    }
}
