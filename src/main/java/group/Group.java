package group;

import user.UserInGroup;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "group_id")
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "group"
    )
    private Set<UserInGroup> users = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "group_scope",
            nullable = false, updatable = false)
    private GroupScope scope;

    protected Group() {
    }

    public Group(String name) {
        this.name = name;
        this.scope = GroupScope.PRIVATE;
    }

    public Group(String name, GroupScope scope) {
        this.name = name;
        this.scope = scope;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserInGroup> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public void addUserToGroup(UserInGroup user) {
        if (scope == GroupScope.PRIVATE && users.size() >= 2) {
            throw new IllegalArgumentException();
        }
        users.add(user);
    }

    public void removeUser(UserInGroup user) {
        users.remove(user);
    }

    public GroupScope getScope() {
        return scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        return id.equals(group.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public enum GroupScope {
        PRIVATE,
        PUBLIC
    }
}
