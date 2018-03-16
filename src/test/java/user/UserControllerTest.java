package user;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import util.EntityManagerFactoryControler;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserControllerTest {
    private List<User> users = new ArrayList<>();

    @Test
    public void createUserTest() {
        User user = UserController.createUser("sashen'ka", "Oleksandr", "Radchykov");
        users.add(user);

        Assert.assertNotNull(user.getId());

        User userFromDatabase = findUser(user.getId());

        assertUserEquals(user, userFromDatabase);
    }

    @Test
    public void createSecuredUserTest() {
        SecuredUser user = UserController.createSecuredUser(
                "sashen'ka", "Oleksandr", "Radchykov", "sasha");
        users.add(user);

        Assert.assertNotNull(user.getId());

        SecuredUser userFromDatabase = (SecuredUser) findUser(user.getId());

        assertSecuredUserEquals(user, userFromDatabase);
    }

    @Test
    public void createUserWithEmailTest() {
        UserWithEmail user = UserController.createUserWithEmail(
                "sashen'ka", "Oleksandr", "Radchykov", "sasha@exmp.com");
        users.add(user);

        Assert.assertNotNull(user.getId());

        UserWithEmail userFromDatabase = (UserWithEmail) findUser(user.getId());

        assertUserWithEmailEquals(user, userFromDatabase);
    }

    @Test
    public void createUserWithWrongEmail() {
        try {
            UserController.createUserWithEmail(
                    "sashen'ka", "Oleksandr", "Radchykov", "sasha.exmp.com");
            Assert.assertFalse(true);
        } catch (IllegalArgumentException ex) {
            List<User> usersFromDatabase = queryUsers();
            Assert.assertEquals(0, usersFromDatabase.size());
        } catch (Exception ex) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void createUserWithPhotosTest() {
        Image photo = new Image("photos/passport", ".png", 300, 400);
        UserWithPhotos user = UserController.createUserWithPhotos(
                "sashen'ka", "Oleksandr", "Radchykov", "myPass", photo);
        users.add(user);

        Assert.assertNotNull(user.getId());

        UserWithPhotos userFromDatabase = (UserWithPhotos) findUser(user.getId());

        assertUserWithPhotosEquals(user, userFromDatabase);
    }

    //@Test
    public void batchPersistUsersTest() {
        users.add(UserController.createTransientUser("sashen'ka1", "Oleksandr", "Radchykov"));
        users.add(UserController.createTransientSecuredUser(
                "sashen'ka2", "Oleksandr", "Radchykov", "sasha"));
        users.add(UserController.createTransientUserWithEmail(
                "sashen'ka3", "Oleksandr", "Radchykov", "sasha@exmp.com"));
        Image photo = new Image("photos/passport", ".png", 300, 400);
        users.add(UserController.createTransientUserWithPhotos(
                "sashen'ka4", "Oleksandr", "Radchykov", "myPass", photo));

        UserController.batchPersistUsers(users);

        List<User> actualUsers = queryUsers();

        assertUsersEquals(users, actualUsers);
    }

    //@Test
    public void getPersistentUsersTest() {
        users.add(UserController.createTransientUser("sashen'ka1", "Oleksandr", "Radchykov"));
        users.add(UserController.createTransientUser("sashen'ka2", "Oleksandr", "Radchykov"));

        UserController.batchPersistUsers(users);

        List<User> actualUsers = UserController.getPersistentUsers();

        assertUsersEquals(users, actualUsers);
    }

    //@Test
    public void getPersistentSecuredUsersTest() {
        users.add(UserController.createTransientSecuredUser(
                "sashen'ka1", "Oleksandr", "Radchykov", "sasha"));
        users.add(UserController.createTransientSecuredUser(
                "sashen'ka2", "Oleksandr", "Radchykov", "sasha"));

        UserController.batchPersistUsers(users);

        List<User> actualUsers = UserController.getPersistentSecuredUsers();

        assertUsersEquals(users, actualUsers);
    }

    //@Test
    public void getPersistentUsersWithEmailTest() {
        users.add(UserController.createTransientUserWithEmail(
                "sashen'ka1", "Oleksandr", "Radchykov", "sasha@exmp.com"));
        users.add(UserController.createTransientUserWithEmail(
                "sashen'ka2", "Oleksandr", "Radchykov", "sasha@exmp.com"));

        UserController.batchPersistUsers(users);

        List<User> actualUsers = UserController.getPersistentUsersWithEmail();

        assertUsersEquals(users, actualUsers);
    }

    //@Test
    public void getPersistentUsersWithPhotosTest() {
        Image photo = new Image("photos/passport", ".png", 300, 400);
        users.add(UserController.createTransientUserWithPhotos(
                "sashen'ka1", "Oleksandr", "Radchykov", "myPass", photo));
        users.add(UserController.createTransientUserWithPhotos(
                "sashen'ka2", "Oleksandr", "Radchykov", "myPass", photo));

        UserController.batchPersistUsers(users);

        List<User> actualUsers = UserController.getPersistentUsersWithPhotos();

        assertUsersEquals(users, actualUsers);
    }

    @Test
    public void deleteUserTest() {
        User user = UserController.createUser("sashen'ka", "Oleksandr", "Radchykov");
        users.add(user);
        Long id = user.getId();

        Assert.assertNotNull(user.getId());

        UserController.deleteUser(user);

        Assert.assertNull(user.getId());
        Assert.assertNull(findUser(id));

        users.remove(0);
    }

    @Test
    public void setNameTest() {
        User user = UserController.createUser("sashen'ka", "Oleksandr", "Radchykov");
        users.add(user);

        UserController.setName(user, "Sasha", "Rad");

        User userFromDatabase = findUser(user.getId());

        assertUserEquals(user, userFromDatabase);
    }

    @Test
    public void setPriorityTest() {
        User user = UserController.createUser("sashen'ka", "Oleksandr", "Radchykov");
        users.add(user);

        UserController.setPriority(user, 0);

        User userFromDatabase = findUser(user.getId());

        assertUserEquals(user, userFromDatabase);
    }

    @Test
    public void setPasswordTest() {
        SecuredUser user = UserController.createSecuredUser(
                "sashen'ka", "Oleksandr", "Radchykov", "sasha");
        users.add(user);

        UserController.setPassword(user, "super_pass");

        User userFromDatabase = findUser(user.getId());

        assertUserEquals(user, userFromDatabase);
    }

    @Test
    public void addEmailTest() {
        UserWithEmail user = UserController.createUserWithEmail(
                "sashen'ka", "Oleksandr", "Radchykov", "sasha@exmp.com");
        users.add(user);

        UserController.addEmail(user, "radchykov@exmp.com");

        User userFromDatabase = findUser(user.getId());

        assertUserEquals(user, userFromDatabase);
    }

    @Test
    public void removeEmailTest() {
        String email = "sasha@exmp.com";
        UserWithEmail user = UserController.createUserWithEmail(
                "sashen'ka", "Oleksandr", "Radchykov", email);
        users.add(user);

        UserController.removeEmail(user, email);

        User userFromDatabase = findUser(user.getId());

        assertUserEquals(user, userFromDatabase);
    }

    @Test
    public void addPhotoTest() {
        Image photo = new Image("photos/passport", ".png", 300, 400);
        UserWithPhotos user = UserController.createUserWithPhotos(
                "sashen'ka", "Oleksandr", "Radchykov", "myPass", photo);
        users.add(user);

        Image newPhoto = new Image("photos/italy/beach", ".jpg");
        UserController.addPhoto(user, "Italy Beach", newPhoto);

        User userFromDatabase = findUser(user.getId());

        assertUserEquals(user, userFromDatabase);
    }

    @Test
    public void removePhotoTest() {
        String photoName = "myPass";
        Image photo = new Image("photos/passport", ".png", 300, 400);
        UserWithPhotos user = UserController.createUserWithPhotos(
                "sashen'ka", "Oleksandr", "Radchykov", photoName, photo);
        users.add(user);

        UserController.removePhoto(user, photoName);

        User userFromDatabase = findUser(user.getId());

        assertUserEquals(user, userFromDatabase);
    }

    //@Test
    public void batchUpdateUsersTest() {
        users.add(UserController.createUser("sashen'ka1", "Oleksandr", "Radchykov"));
        users.add(UserController.createSecuredUser(
                "sashen'ka2", "Oleksandr", "Radchykov", "sasha"));
        users.add(UserController.createUserWithEmail(
                "sashen'ka3", "Oleksandr", "Radchykov", "sasha@exmp.com"));
        String photoName = "myPass";
        Image photo = new Image("photos/passport", ".png", 300, 400);
        users.add(UserController.createUserWithPhotos(
                "sashen'ka4", "Oleksandr", "Radchykov", photoName, photo));

        UserController.setDetachedName(users.get(0), "Sasha", "Rad");
        UserController.setDetachedPassword((SecuredUser) users.get(1), "super_pass");
        UserController.addDetachedEmail((UserWithEmail) users.get(2), "radchykov@exmp.com");
        UserController.removeDetachedPhoto((UserWithPhotos) users.get(3), photoName);

        UserController.batchUpdateUsers(users);

        List<User> actualUsers = queryUsers();

        assertUsersEquals(users, actualUsers);
    }

    @After
    public void deleteUserAfterTest() {
        for (User user : users) {
            if (user.getId() != null && findUser(user.getId()) != null) {
                UserController.deleteUser(user);
            }
        }
        users = new ArrayList<>();
    }

    private User findUser(Long id) {
        EntityManager em = EntityManagerFactoryControler.getLocalEntityManager();
        try {
            User user = em.find(User.class, id);
            if (user == null) {
                return null;
            }
            if (user.getClass().equals(UserWithEmail.class)) {
                UserWithEmail userWithEmail = (UserWithEmail) user;
                userWithEmail.getEmails().size();
                return userWithEmail;
            }
            if (user.getClass().equals(UserWithPhotos.class)) {
                UserWithPhotos userWithPhotos = (UserWithPhotos) user;
                userWithPhotos.getPhotos().size();
                return userWithPhotos;
            }
            return user;
        } finally {
            em.close();
        }
    }

    private List<User> queryUsers() {
        EntityManager em = EntityManagerFactoryControler.getLocalEntityManager();
        try {
            List<User> users = em.createQuery("from User", User.class).getResultList();
            for (User user : users) {
                if (user.getClass().equals(UserWithEmail.class)) {
                    ((UserWithEmail) user).getEmails().size();
                }
                if (user.getClass().equals(UserWithPhotos.class)) {
                    ((UserWithPhotos) user).getPhotos().size();
                }
            }
            return users;
        } finally {
            em.close();
        }
    }

    private void assertUserEquals(User first, User second) {
        Assert.assertEquals(first.getClass(), second.getClass());
        Assert.assertEquals(first.getCredentials(), second.getCredentials());
        Assert.assertEquals(first.getCredentials().getName(), second.getCredentials().getName());
        Assert.assertEquals(first.getPriority(), second.getPriority());
    }

    private void assertSecuredUserEquals(SecuredUser first, SecuredUser second) {
        assertUserEquals(first, second);
        Assert.assertEquals(first.getPassword(), second.getPassword());
    }

    private void assertUserWithEmailEquals(UserWithEmail first, UserWithEmail second) {
        assertUserEquals(first, second);
        Assert.assertEquals(first.getEmails(), second.getEmails());
    }

    private void assertUserWithPhotosEquals(UserWithPhotos first, UserWithPhotos second) {
        assertUserEquals(first, second);
        Assert.assertEquals(first.getPhotos(), second.getPhotos());
    }

    private void anyTypeUserEquals(User first, User second) {
        if (first.getClass() == User.class && second.getClass() == User.class) {
            assertUserEquals(first, second);

        } else if (first.getClass() == SecuredUser.class && second.getClass() == SecuredUser.class) {
            assertSecuredUserEquals((SecuredUser) first, (SecuredUser) second);

        } else if (first.getClass() == UserWithEmail.class && second.getClass() == UserWithEmail.class) {
            assertUserWithEmailEquals((UserWithEmail) first, (UserWithEmail) second);

        } else if (first.getClass() == UserWithPhotos.class && second.getClass() == UserWithPhotos.class) {
            assertUserWithPhotosEquals((UserWithPhotos) first, (UserWithPhotos) second);
        } else {
            Assert.assertFalse(true);
        }
    }

    private void assertUsersEquals(List<User> firstListUsers, List<User> secondListUsers) {
        Assert.assertEquals(firstListUsers.size(), secondListUsers.size());
        for (User secondUser : secondListUsers) {
            Optional<User> firstUser = firstListUsers.stream().filter(secondUser::equals).findFirst();
            Assert.assertTrue(firstUser.isPresent());
            anyTypeUserEquals(firstUser.get(), secondUser);
        }
    }
}
