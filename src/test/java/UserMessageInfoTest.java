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
        Group family = new Group("family");
        UserInGroup sashaInFamily = new UserInGroup(family, userSasha);
        Group work = new Group("work");
        UserInGroup sashaInWork = new UserInGroup(work, userSasha);

        try {
            em.getTransaction().begin();

            em.persist(userSasha);
            em.persist(family);
            em.persist(work);
            sashaInFamily.writeMessage("Concur the world");
            em.persist(sashaInFamily);
            sashaInWork.writeMessage("Concur the world");
            em.persist(sashaInWork);

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
        assertEquals(new Integer(2), userMessageInfo.getMessageCount());
    }
}
