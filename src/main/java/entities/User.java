package entities;

import org.hibernate.annotations.Cascade;
import values.Credentials;

import javax.persistence.*;
import java.util.*;

/**
 * Created by oradchykova on 8/21/17.
 */
@Entity
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

    @OneToMany(mappedBy = "creator",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderColumn(
            name = "message_position"
    )
    private List<Message> messages = new ArrayList<>();

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

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public Message writeMessage(String text) {
        Message message = new Message(text, this);
        messages.add(message);
        return message;
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

        if (priority != user.priority) return false;
        if (!credentials.equals(user.credentials)) return false;
        if (!messages.equals(user.messages)) return false;
        return device != null ? device.equals(user.device) : user.device == null;
    }

    @Override
    public int hashCode() {
        int result = credentials.hashCode();
        result = 31 * result + messages.hashCode();
        result = 31 * result + (device != null ? device.hashCode() : 0);
        result = 31 * result + priority;
        return result;
    }
}
