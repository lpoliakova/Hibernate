package util;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@org.hibernate.annotations.Subselect(value =
        "select u.ID as userId, u.LOGIN as userLogin, COUNT(m.ID) as messageCount " +
                "from USER u left join MESSAGE m on u.ID = m.USERID " +
                "group by u.ID")
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
