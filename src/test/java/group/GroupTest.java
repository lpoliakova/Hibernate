package group;

import user.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.Credentials;
import user.UserName;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GroupTest {
    private EntityManagerFactory emf;

    @Test
    public void groupTest() {
        Group group = new Group("family");

        persistGroups(group);

        EntityManager em = emf.createEntityManager();
        Group actual = em.find(Group.class, group.getId());

        assertGroupEquals(group, actual);

        em.close();
    }

    @Test
    public void privateGroupTest() {
        Group group = new Group("family");
        User lena = new User(new Credentials("lena", new UserName("Lena", "P")));
        User hash = new User(new Credentials("hash", new UserName("Hash", "R")));
        UserInGroup lenaInFamily = new UserInGroup(group, lena);
        UserInGroup hashInFamily = new UserInGroup(group, hash);

        User sasha = new User(new Credentials("sasha", new UserName("Sasha", "R")));
        UserInGroup sashaInFamily = new UserInGroup(group, sasha);

        try {
            persistGroups(group);
            persistUsers(lena, hash, sasha);
            persistGroupUserConnection(lenaInFamily, hashInFamily, sashaInFamily);

            assertTrue(false);
        } catch (RollbackException ex) {
            assertTrue(true);
        }

        try {
            group.addUserToGroup(lenaInFamily);
            lena.addGroup(lenaInFamily);
            group.addUserToGroup(hashInFamily);
            hash.addGroup(hashInFamily);
            group.addUserToGroup(sashaInFamily);
            sasha.addGroup(sashaInFamily);

            assertTrue(false);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }

        EntityManager em = emf.createEntityManager();
        Group actual = em.find(Group.class, group.getId());

        Assert.assertEquals(group.getUsers(), actual.getUsers());

        em.close();
    }

    @Test
    public void groupUserTest() {
        Group group = new Group("family", Group.GroupScope.PUBLIC);
        User lena = new User(new Credentials("lena", new UserName("Lena", "P")));
        User hash = new User(new Credentials("hash", new UserName("Hash", "R")));
        UserInGroup lenaInFamily = new UserInGroup(group, lena);
        UserInGroup hashInFamily = new UserInGroup(group, hash);

        User sasha = new User(new Credentials("sasha", new UserName("Sasha", "R")));
        UserInGroup sashaInFamily = new UserInGroup(group, sasha);

        persistGroups(group);
        persistUsers(lena, hash, sasha);
        persistGroupUserConnection(lenaInFamily, hashInFamily, sashaInFamily);

        group.addUserToGroup(lenaInFamily);
        lena.addGroup(lenaInFamily);
        group.addUserToGroup(hashInFamily);
        hash.addGroup(hashInFamily);
        group.addUserToGroup(sashaInFamily);
        sasha.addGroup(sashaInFamily);

        EntityManager em = emf.createEntityManager();
        Group actual = em.find(Group.class, group.getId());

        Assert.assertEquals(group.getUsers(), actual.getUsers());

        em.close();
    }

    @Test
    public void userGroupTest() {
        User sasha = new User(new Credentials("sasha", new UserName("Sasha", "R")));
        Group family = new Group("family");
        UserInGroup sashaInFamily = new UserInGroup(family, sasha);
        Group work = new Group("work");
        UserInGroup sashaInWork = new UserInGroup(work, sasha);

        persistGroups(family, work);
        persistUsers(sasha);
        persistGroupUserConnection(sashaInFamily, sashaInWork);

        family.addUserToGroup(sashaInFamily);
        sasha.addGroup(sashaInFamily);
        work.addUserToGroup(sashaInWork);
        sasha.addGroup(sashaInWork);

        EntityManager em = emf.createEntityManager();
        User actual = em.find(User.class, sasha.getId());

        Assert.assertEquals(sasha.getGroups(), actual.getGroups());

        em.close();
    }

    private void persistGroups(Group ... groups) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            for (Group group : groups) {
                em.persist(group);
            }

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    private void persistUsers(User ... users) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            for (User user : users) {
                em.persist(user);
            }

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    private void persistGroupUserConnection(UserInGroup ... groupsUsers) {

        for (UserInGroup groupsUser : groupsUsers) {
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();

                em.find(Group.class, groupsUser.getGroup().getId());
                em.find(User.class, groupsUser.getUser().getId());
                em.merge(groupsUser);

                em.getTransaction().commit();
            } finally {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }
    }


    private void assertGroupEquals(Group first, Group second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getScope(), second.getScope());
        assertEquals(first.getUsers(), second.getUsers());
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
