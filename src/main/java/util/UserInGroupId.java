package util;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserInGroupId implements Serializable{

    @Column(name = "fk_groups")
    private Long groupId;

    @Column(name = "fk_user")
    private Long userId;

    public UserInGroupId(){}

    public UserInGroupId(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInGroupId that = (UserInGroupId) o;

        if (!groupId.equals(that.groupId)) return false;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Long getUserId() {
        return userId;
    }
}
