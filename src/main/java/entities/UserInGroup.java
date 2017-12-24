package entities;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import util.UserInGroupId;

import javax.persistence.*;

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

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInGroup that = (UserInGroup) o;

        if (!id.equals(that.id)) return false;
        if (muted != null ? !muted.equals(that.muted) : that.muted != null) return false;
        if (!group.equals(that.group)) return false;
        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (muted != null ? muted.hashCode() : 0);
        result = 31 * result + group.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}
