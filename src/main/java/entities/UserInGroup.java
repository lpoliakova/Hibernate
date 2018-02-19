package entities;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import util.UserInGroupId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "groups_users")
public class UserInGroup {

    @EmbeddedId
    private UserInGroupId id;

    @ColumnDefault("true")
    @Generated(GenerationTime.INSERT)
    @Column(nullable = false)
    private Boolean muted;

    @MapsId("groupId")
    @ManyToOne
    private Group group;

    @MapsId("userId")
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "place",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderColumn(
            name = "message_position"
    )
    private List<Message> messages = new ArrayList<>();

    protected UserInGroup() {

    }

    public UserInGroup(Group group, User user) {
        this.group = group;
        this.user = user;
        this.id = new UserInGroupId(group.getId(), user.getId());
    }

    public UserInGroup(Group group, User user, Boolean muted) {
        this(group, user);
        this.muted = muted;
    }

    public UserInGroupId getId() {
        return id;
    }

    public Boolean getMuted() {
        return muted;
    }

    public Group getGroup() {
        return group;
    }

    public User getUser() {
        return user;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public Message writeMessage(String text) {
        Message message = new Message(text, this);
        messages.add(message);
        return message;
    }


    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInGroup that = (UserInGroup) o;

        if (!id.equals(that.id)) return false;
        if (!group.equals(that.group)) return false;
        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + group.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}
