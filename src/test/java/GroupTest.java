import entities.Group;
import entities.User;
import entities.UserInGroup;
import org.junit.Assert;
import org.junit.Test;
import values.Credentials;
import values.UserName;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GroupTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("myTest");

    @Test
    public void groupTest() {
        EntityManager em = emf.createEntityManager();

        Group group = new Group("family");

        try {
            em.getTransaction().begin();

            em.persist(group);

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        Group actual = em.find(Group.class, group.getId());

        Assert.assertEquals(group.getName(), actual.getName());
        Assert.assertEquals(group.getUsers(), actual.getUsers());
    }

    @Test
    public void groupUserTest() {
        EntityManager em = emf.createEntityManager();
        Group group = new Group("family");
        User lena = new User(new Credentials("lena", new UserName("Lena", "P")));
        UserInGroup lenaInFamily = new UserInGroup(group, lena);

        User sasha = new User(new Credentials("sasha", new UserName("Sasha", "R")));
        UserInGroup sashaInFamily = new UserInGroup(group, sasha);


        try {
            em.getTransaction().begin();

            em.persist(group);
            em.persist(lena);
            em.persist(sasha);
            em.persist(lenaInFamily);
            em.persist(sashaInFamily);

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        group.addUserToGroup(lenaInFamily);
        lena.addGroup(lenaInFamily);
        group.addUserToGroup(sashaInFamily);
        sasha.addGroup(sashaInFamily);

        Group actual = em.find(Group.class, group.getId());

        Assert.assertEquals(group.getUsers(), actual.getUsers());
    }

    @Test
    public void userGroupTest() {
        EntityManager em = emf.createEntityManager();
        User sasha = new User(new Credentials("sasha", new UserName("Sasha", "R")));
        Group family = new Group("family");
        UserInGroup sashaInFamily = new UserInGroup(family, sasha);
        Group work = new Group("work");
        UserInGroup sashaInWork = new UserInGroup(work, sasha);

        try {
            em.getTransaction().begin();

            em.persist(family);
            em.persist(work);
            em.persist(sasha);
            em.persist(sashaInFamily);
            em.persist(sashaInWork);

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        family.addUserToGroup(sashaInFamily);
        sasha.addGroup(sashaInFamily);
        work.addUserToGroup(sashaInWork);
        sasha.addGroup(sashaInWork);

        User actual = em.find(User.class, sasha.getId());

        Assert.assertEquals(sasha.getGroups(), actual.getGroups());
    }

}
