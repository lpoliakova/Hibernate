package group;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import user.User;
import user.UserController;
import util.EntityManagerFactoryController;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class GroupControllerTest {
    private List<User> users = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();

    @Test
    public void createPublicGroupTest() {
        User user = UserController.createUser("sashen'ka", "Oleksandr", "Radchykov");
        users.add(user);

        Group group = GroupController.createPublicGroup("family", user);
        groups.add(group);

        Assert.assertNotNull(group.getId());

        Group groupFromDatabase = findGroup(group.getId());

        assertGroupEquals(group, groupFromDatabase);
    }

    @Test
    public void createPrivateGroupTest() {
        users.add(UserController.createUser("sashen'ka", "Oleksandr", "Radchykov"));
        users.add(UserController.createUser("lena", "Lena", "Poliakova"));

        Group group = GroupController.createPrivateGroup("family", users.get(0), users.get(1));
        groups.add(group);

        Assert.assertNotNull(group.getId());

        Group groupFromDatabase = findGroup(group.getId());

        assertGroupEquals(group, groupFromDatabase);
    }

    //@Test
    public void batchPersistGroupsTest() {
        groups.add(GroupController.createTransientPrivateGroup("family"));
        groups.add(GroupController.createTransientPublicGroup("dog chat"));

        GroupController.batchPersistGroups(groups);

        List<Group> actualGroups = queryGroups();

        assertGroupsEquals(groups, actualGroups);
    }

    //@Test
    public void getPersistentGroupsTest() {
        users.add(UserController.createUser("sashen'ka", "Oleksandr", "Radchykov"));
        users.add(UserController.createUser("lena", "Lena", "Poliakova"));
        users.add(UserController.createUser("hash", "Hash", "Radchykov"));

        groups.add(GroupController.createPrivateGroup("family", users.get(0), users.get(1)));
        groups.add(GroupController.createPublicGroup("dog chat", users.get(2)));

        List<Group> actualGroups = GroupController.getPersistentGroups();

        assertGroupsEquals(groups, actualGroups);
    }

    @Test
    public void deleteGroupTest() {
        users.add(UserController.createUser("sashen'ka", "Oleksandr", "Radchykov"));
        Group group = GroupController.createPublicGroup("family", users.get(0));
        groups.add(group);
        Long id = group.getId();

        Assert.assertNotNull(group.getId());

        GroupController.deleteGroup(group);

        Assert.assertNull(group.getId());
        Assert.assertNull(findGroup(id));

        groups.remove(0);
    }

    @Test
    public void setNameTest() {
        users.add(UserController.createUser("sashen'ka", "Oleksandr", "Radchykov"));
        Group group = GroupController.createPublicGroup("family", users.get(0));
        groups.add(group);

        GroupController.setName(group, "work");

        Group groupFromDatabase = findGroup(group.getId());

        assertGroupEquals(group, groupFromDatabase);
    }

    @Test
    public void setMutedTest() {
        User user = UserController.createUser("sashen'ka", "Oleksandr", "Radchykov");
        users.add(user);

        Group group = GroupController.createPublicGroup("family", user);
        groups.add(group);

        GroupController.setMuted(group, user, true);

        Group groupFromDatabase = findGroup(group.getId());

        assertGroupEquals(group, groupFromDatabase);
    }

    //@Test
    public void batchUpdateGroupsTest() {
        users.add(UserController.createUser("sashen'ka", "Oleksandr", "Radchykov"));
        users.add(UserController.createUser("lena", "Lena", "Poliakova"));
        users.add(UserController.createUser("hash", "Hash", "Radchykov"));

        groups.add(GroupController.createPrivateGroup("family", users.get(0), users.get(1)));
        groups.add(GroupController.createPublicGroup("dog chat", users.get(2)));

        GroupController.setDetachedName(groups.get(0), "work");
        GroupController.setDetachedMuted(groups.get(1).getUserInGroupByUser(users.get(2)), true);

        GroupController.batchUpdateGroups(groups);

        List<Group> actualGroups = queryGroups();

        assertGroupsEquals(groups, actualGroups);
    }

    @Test
    public void addUserTest() {
        users.add(UserController.createUser("sashen'ka", "Oleksandr", "Radchykov"));
        users.add(UserController.createUser("lena", "Lena", "Poliakova"));

        Group group = GroupController.createPublicGroup("family", users.get(0));
        groups.add(group);

        GroupController.addUser(group, users.get(1));

        Group groupFromDatabase = findGroup(group.getId());

        assertGroupEquals(group, groupFromDatabase);
    }

    @Test
    public void addThirdUserToPrivateGroupTest() {
        users.add(UserController.createUser("sashen'ka", "Oleksandr", "Radchykov"));
        users.add(UserController.createUser("lena", "Lena", "Poliakova"));
        users.add(UserController.createUser("hash", "Hash", "Radchykov"));

        Group group = GroupController.createPrivateGroup("family", users.get(0), users.get(1));
        groups.add(group);

        try {
            GroupController.addUser(group, users.get(2));
            Assert.assertTrue(false);
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(true);
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

        Assert.assertEquals(2, group.getUsers().size());
    }

    //@Test
    public void batchAddUsersTest() {
        users.add(UserController.createUser("sashen'ka", "Oleksandr", "Radchykov"));
        users.add(UserController.createUser("lena", "Lena", "Poliakova"));
        users.add(UserController.createUser("hash", "Hash", "Radchykov"));

        Group group = GroupController.createPublicGroup("family", users.get(0));
        groups.add(group);

        List<UserInGroup> addedUsers = new ArrayList<>(2);
        addedUsers.add(GroupController.addDetachedUser(group, users.get(1)));
        addedUsers.add(GroupController.addDetachedUser(group, users.get(2)));

        GroupController.batchAddUsers(addedUsers);

        Group groupFromDatabase = findGroup(group.getId());

        assertGroupEquals(group, groupFromDatabase);
    }

    @Test
    public void removeUserTest() {
        users.add(UserController.createUser("sashen'ka", "Oleksandr", "Radchykov"));
        users.add(UserController.createUser("lena", "Lena", "Poliakova"));

        Group group = GroupController.createPrivateGroup("family", users.get(0), users.get(1));
        groups.add(group);

        GroupController.removeUser(group, users.get(1));

        Group groupFromDatabase = findGroup(group.getId());

        assertGroupEquals(group, groupFromDatabase);
    }

    //@Test
    public void batchRemoveUsersTest() {
        users.add(UserController.createUser("sashen'ka", "Oleksandr", "Radchykov"));
        users.add(UserController.createUser("lena", "Lena", "Poliakova"));
        users.add(UserController.createUser("hash", "Hash", "Radchykov"));

        Group group = GroupController.createPublicGroup("family", users.get(0));
        groups.add(group);

        GroupController.addUser(group, users.get(1));
        GroupController.addUser(group, users.get(2));

        List<UserInGroup> removedUsers = new ArrayList<>(2);
        removedUsers.add(GroupController.removeDetachedUser(group, users.get(0)));
        removedUsers.add(GroupController.removeDetachedUser(group, users.get(1)));

        GroupController.batchRemoveUsers(removedUsers);

        Group groupFromDatabase = findGroup(group.getId());

        assertGroupEquals(group, groupFromDatabase);
    }

    @After
    public void deleteUsersAndGroupsAfterTest() {
        for (Group group : groups) {
            if (group.getId() != null && findGroup(group.getId()) != null) {
                GroupController.deleteGroup(group);
            }
        }
        groups = new ArrayList<>();

        for (User user : users) {
            if (user.getId() != null && findUser(user.getId()) != null) {
                UserController.deleteUser(user);
            }
        }
        users = new ArrayList<>();
    }

    private User findUser(Long id) {
        EntityManager em = EntityManagerFactoryController.getLocalEntityManager();
        try {
            User user = em.find(User.class, id);
            if (user == null) {
                return null;
            }
            user.getGroups().size();
            return user;
        } finally {
            em.close();
        }
    }

    private Group findGroup(Long id) {
        EntityManager em = EntityManagerFactoryController.getLocalEntityManager();
        try {
            Group group = em.find(Group.class, id);
            if (group == null) {
                return null;
            }
            group.getUsers().size();
            return group;
        } finally {
            em.close();
        }
    }

    private List<Group> queryGroups() {
        EntityManager em = EntityManagerFactoryController.getLocalEntityManager();
        try {
            List<Group> groups = em.createQuery("from Group", Group.class).getResultList();
            groups.forEach(g -> g.getUsers().size());
            return groups;
        } finally {
            em.close();
        }
    }

    private void assertGroupEquals(Group first, Group second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getScope(), second.getScope());
        assertEquals(first.getUsers(), second.getUsers());
    }

    private void assertGroupsEquals(List<Group> firstListGroups, List<Group> secondListGroups) {
        Assert.assertEquals(firstListGroups.size(), secondListGroups.size());
        for (Group secondGroup : secondListGroups) {
            Optional<Group> firstUser = firstListGroups.stream().filter(secondGroup::equals).findFirst();
            Assert.assertTrue(firstUser.isPresent());
            assertGroupEquals(firstUser.get(), secondGroup);
        }
    }
}
