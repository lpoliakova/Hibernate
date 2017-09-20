package PersistentClasses;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by oradchykova on 7/8/17.
 */
@Entity
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;
    private String text;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
