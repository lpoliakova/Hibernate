package entities;

import values.Credentials;

import javax.persistence.*;
import java.util.*;

/**
 * Created by oradchykova on 8/21/17.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private Credentials credentials;

    @OneToMany(
            mappedBy = "user"
    )
    private Set<UserInGroup> groups = new HashSet<>();

    @OneToOne (
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    private Device device;

    @Column(nullable = false)
    @org.hibernate.annotations.ColumnTransformer(
            forColumn = "priority",
            read = "priority - 1",
            write = "? + 1"
    )
    private int priority;

    protected User(){}

    public User(Credentials credentials){
        this.credentials = credentials;
        priority = 10;
    }

    public Long getId() {
        return id;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Set<UserInGroup> getGroups() {
        return groups;
//        return Collections.unmodifiableSet(groups);
    }

    public void addGroup(UserInGroup group) {
        groups.add(group);
    }

    public void removeGroup(UserInGroup group) {
        groups.remove(group);
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        return credentials.equals(user.credentials);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + credentials.hashCode();
        return result;
    }
}
