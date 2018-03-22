package user;

import com.google.common.base.Strings;
import device.Device;
import group.UserInGroup;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.EntityManagerFactoryController;

import java.util.List;

public class UserController {
    //TODO: how to check uniqueness in application layer?
    //TODO: how to update image properly?

    public static User createTransientUser(String login, String firstName, String lastName) throws IllegalArgumentException {
        Credentials credentials = createCredentials(login, firstName, lastName);
        return new User(credentials);
    }

    public static User createUser(String login, String firstName, String lastName) throws IllegalArgumentException {
        User user = createTransientUser(login, firstName, lastName);
        EntityManagerFactoryController.persist(user);
        return user;
    }

    public static SecuredUser createTransientSecuredUser(String login, String firstName, String lastName, String password) throws IllegalArgumentException {
        if (Strings.isNullOrEmpty(password)) {
            throw new IllegalArgumentException();
        }

        Credentials credentials = createCredentials(login, firstName, lastName);
        return new SecuredUser(credentials, password);
    }

    public static SecuredUser createSecuredUser(String login, String firstName, String lastName, String password) throws IllegalArgumentException {
        SecuredUser user = createTransientSecuredUser(login, firstName, lastName, password);
        EntityManagerFactoryController.persist(user);
        return user;
    }

    public static UserWithEmail createTransientUserWithEmail(String login, String firstName, String lastName, String email) throws IllegalArgumentException {
        if (Strings.isNullOrEmpty(email)) {
            throw new IllegalArgumentException();
        }
        if (email.indexOf('@') <= 0) {
            throw new IllegalArgumentException("Email is incorrect");
        }

        Credentials credentials = createCredentials(login, firstName, lastName);
        UserWithEmail user = new UserWithEmail(credentials);
        user.addEmail(email);
        return user;
    }

    public static UserWithEmail createUserWithEmail(String login, String firstName, String lastName, String email) throws IllegalArgumentException {
        UserWithEmail user = createTransientUserWithEmail(login, firstName, lastName, email);
        EntityManagerFactoryController.persist(user);
        return user;
    }

    public static UserWithPhotos createTransientUserWithPhotos(String login, String firstName, String lastName, String photoName, Image photo) throws IllegalArgumentException {
        if (Strings.isNullOrEmpty(photoName) || photo == null) {
            throw new IllegalArgumentException();
        }

        Credentials credentials = createCredentials(login, firstName, lastName);
        UserWithPhotos user = new UserWithPhotos(credentials);
        user.addPhoto(photoName, photo);
        return user;
    }

    public static UserWithPhotos createUserWithPhotos(String login, String firstName, String lastName, String photoName, Image photo) throws IllegalArgumentException {
        UserWithPhotos user = createTransientUserWithPhotos(login, firstName, lastName, photoName, photo);
        EntityManagerFactoryController.persist(user);
        return user;
    }

    public static void batchPersistUsers(List<User> transientUsers) {
        //TODO: implement batch persist
    }

    public static List<User> getPersistentUsers() {
        //TODO: implement getPersistentUsers
        throw new NotImplementedException();
    }

    public static List<User> getPersistentSecuredUsers() {
        //TODO: implement getPersistentSecuredUsers
        throw new NotImplementedException();
    }

    public static List<User> getPersistentUsersWithEmail() {
        //TODO: implement getPersistentUsersWithEmail
        throw new NotImplementedException();
    }

    public static List<User> getPersistentUsersWithPhotos() {
        //TODO: implement getPersistentUsersWithPhotos
        throw new NotImplementedException();
    }

    public static void deleteUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        EntityManagerFactoryController.delete(user);
    }

    public static void setDetachedName(User user, String firstName, String lastName) {
        if (user == null
                || Strings.isNullOrEmpty(firstName)
                || Strings.isNullOrEmpty(lastName)) {
            throw new IllegalArgumentException();
        }
        UserName name = new UserName(firstName, lastName);
        user.getCredentials().setName(name);
    }

    public static void setName(User user, String firstName, String lastName) {
        setDetachedName(user, firstName, lastName);
        EntityManagerFactoryController.update(user);
    }

    public static void setDetachedPriority(User user, int priority) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        user.setPriority(priority);
    }

    public static void setPriority(User user, int priority) {
        setDetachedPriority(user, priority);
        EntityManagerFactoryController.update(user);
    }

    public static void setDetachedDevice(User user, Device device) {
        if (user == null || device == null) {
            throw new IllegalArgumentException();
        }
        user.setDevice(device);
    }

    public static void removeDetachedDevice(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        user.setDevice(null);
    }

    public static void addDetachedGroup(User user, UserInGroup group) {
        if (user == null || group == null) {
            throw new IllegalArgumentException();
        }
        user.addGroup(group);
    }

    public static void removeDetachedGroup(User user, UserInGroup group) {
        if (user == null || group == null) {
            throw new IllegalArgumentException();
        }
        user.removeGroup(group);
    }

    public static void setDetachedPassword(SecuredUser user, String password) {
        if (user == null || Strings.isNullOrEmpty(password)) {
            throw new IllegalArgumentException();
        }

        user.setPassword(password);
    }

    public static void setPassword(SecuredUser user, String password) {
        setDetachedPassword(user, password);
        EntityManagerFactoryController.update(user);
    }

    public static void addDetachedEmail(UserWithEmail user, String email) throws IllegalArgumentException {
        if (user == null
                || Strings.isNullOrEmpty(email)
                || email.indexOf('@') <= 0) {
            throw new IllegalArgumentException("wrong email");
        }

        user.addEmail(email);
    }

    public static void addEmail(UserWithEmail user, String email) throws IllegalArgumentException {
        addDetachedEmail(user, email);
        EntityManagerFactoryController.update(user);
    }

    public static void removeDetachedEmail(UserWithEmail user, String email) {
        if (user == null || Strings.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("wrong email");
        }

        user.removeEmail(email);
    }

    public static void removeEmail(UserWithEmail user, String email) {
        removeDetachedEmail(user, email);
        EntityManagerFactoryController.update(user);
    }

    public static void addDetachedPhoto(UserWithPhotos user, String photoName, Image photo) {
        if (user == null
                || Strings.isNullOrEmpty(photoName)
                || photo == null) {
            throw new IllegalArgumentException();
        }

        user.addPhoto(photoName, photo);
    }

    public static void addPhoto(UserWithPhotos user, String photoName, Image photo) {
        addDetachedPhoto(user, photoName, photo);
        EntityManagerFactoryController.update(user);
    }

    public static void removeDetachedPhoto(UserWithPhotos user, String photoName) {
        if (user == null || Strings.isNullOrEmpty(photoName)) {
            throw new IllegalArgumentException();
        }

        user.removePhoto(photoName);
    }

    public static void removePhoto(UserWithPhotos user, String photoName) {
        removeDetachedPhoto(user, photoName);
        EntityManagerFactoryController.update(user);
    }

    public static void batchUpdateUsers(List<User> detachedUsers) {
        //TODO: implement batch update
    }

    private static Credentials createCredentials(String login, String firstName, String lastName) throws IllegalArgumentException {
        if (Strings.isNullOrEmpty(login)
                || Strings.isNullOrEmpty(firstName)
                || Strings.isNullOrEmpty(lastName)) {
            throw new IllegalArgumentException();
        }

        UserName name = new UserName(firstName, lastName);
        return new Credentials(login, name);
    }
}
