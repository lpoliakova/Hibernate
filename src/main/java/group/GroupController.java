package group;

import com.google.common.base.Strings;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import user.User;
import user.UserController;
import util.EntityManagerFactoryController;

import java.util.List;

public class GroupController {

    public static Group createTransientPublicGroup(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException();
        }
        return new Group(name, Group.GroupScope.PUBLIC);
    }

    public static Group createTransientPrivateGroup(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException();
        }
        return new Group(name, Group.GroupScope.PRIVATE);
    }

    public static Group createPublicGroup(String name, User user) {
        Group group = createTransientPublicGroup(name);

        EntityManagerFactoryController.persist(group);

        UserInGroup userInGroup = createUserInGroup(group, user);
        group.addUser(userInGroup);
        UserController.addDetachedGroup(user, userInGroup);

        EntityManagerFactoryController.update(userInGroup);

        return group;
    }

    public static Group createPrivateGroup(String name, User first, User second) {
        Group group = createTransientPrivateGroup(name);

        EntityManagerFactoryController.persist(group);

        UserInGroup firstInGroup = createUserInGroup(group, first);
        UserInGroup secondInGroup = createUserInGroup(group, second);
        group.addUser(firstInGroup);
        group.addUser(secondInGroup);
        UserController.addDetachedGroup(first, firstInGroup);
        UserController.addDetachedGroup(second, secondInGroup);

        EntityManagerFactoryController.update(firstInGroup, secondInGroup);

        return group;
    }

    public static void batchPersistGroups(List<Group> transientGroups) {
        //TODO: implement batch persist
    }

    public static List<Group> getPersistentGroups() {
        //TODO: implement getPersistentGroups
        throw new NotImplementedException();
    }

    public static void deleteGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException();
        }

        while (!group.getUsers().isEmpty()) {
            UserInGroup userInGroup = group.getUsers().stream().findFirst().get();
            group.removeUser(userInGroup);
            UserController.removeDetachedGroup(userInGroup.getUser(), userInGroup);

            EntityManagerFactoryController.delete(userInGroup);
        } //TODO: rewrite with batch remove users

        EntityManagerFactoryController.delete(group);
    }

    public static void setDetachedName(Group group, String name) {
        if (group == null || Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException();
        }
        group.setName(name);
    }

    public static void setName(Group group, String name) {
        setDetachedName(group, name);
        EntityManagerFactoryController.update(group);
    }

    public static void setDetachedMuted(UserInGroup userInGroup, boolean muted) {
        if (userInGroup == null) {
            throw new IllegalArgumentException();
        }

        userInGroup.setMuted(muted);
    }

    public static void setMuted(UserInGroup userInGroup, boolean muted) {
        setDetachedMuted(userInGroup, muted);
        EntityManagerFactoryController.update(userInGroup);
    }

    public static void setMuted(Group group, User user, boolean muted) {
        UserInGroup userInGroup = createUserInGroup(group, user);
        setMuted(userInGroup, muted);
    }

    public static void batchUpdateGroups(List<Group> detachedGroups) {
        //TODO: implement batch update
    }

    public static UserInGroup addDetachedUser(Group group, User user) {
        if (user == null || group == null) {
            throw new IllegalArgumentException();
        }

        UserInGroup userInGroup = createUserInGroup(group, user);
        UserController.addDetachedGroup(user, userInGroup);
        group.addUser(userInGroup);

        return userInGroup;
    }

    public static void addUser(Group group, User user) {
        if (group.getScope() == Group.GroupScope.PRIVATE) {
            throw new IllegalArgumentException();
        }

        UserInGroup userInGroup = addDetachedUser(group, user);

        EntityManagerFactoryController.update(userInGroup);
    }

    public static void batchAddUsers(List<UserInGroup> detachedUsersInGroup) {
        //TODO: implement batch update
    }

    public static UserInGroup removeDetachedUser(Group group, User user) {
        if (user == null || group == null) {
            throw new IllegalArgumentException();
        }

        UserInGroup userInGroup = createUserInGroup(group, user);
        UserController.removeDetachedGroup(user,userInGroup);
        group.removeUser(userInGroup);

        return userInGroup;
    }

    public static void removeUser(Group group, User user) {
        UserInGroup userInGroup = removeDetachedUser(group, user);

        EntityManagerFactoryController.delete(userInGroup);
    }

    public static void batchRemoveUsers(List<UserInGroup> detachedUsersInGroup) {
        //TODO: implement batch update
    }

    //TODO: implement messages

    private static UserInGroup createUserInGroup(Group group, User user) {
        if (group == null || user == null) {
            throw new IllegalArgumentException();
        }
        return new UserInGroup(group, user);
    }
}
