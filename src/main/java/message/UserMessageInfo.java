package message;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@org.hibernate.annotations.Subselect(value =
        "select u.user_id as userId, u.login as userLogin, COUNT(m.fk_users) as messageCount " +
                "from users u left join messages m on u.user_id = m.fk_users " +
                "group by u.user_id")
@org.hibernate.annotations.Synchronize({"User", "Message"})
public class UserMessageInfo {
    private Long userId;
    private String userLogin;
    private Integer messageCount;

    @Id
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }
}
