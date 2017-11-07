package PersistentClasses;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by oradchykova on 7/8/17.
 */
@Entity
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @Column(insertable = false)
    @org.hibernate.annotations.ColumnDefault("1")
    @org.hibernate.annotations.Generated(
            org.hibernate.annotations.GenerationTime.INSERT
    )
    private Long userId;
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date creationTimestamp;

    @Enumerated(EnumType.STRING)
    private MessageScope scope;

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

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public MessageScope getScope() {
        return scope;
    }

    public void setScope(MessageScope scope) {
        this.scope = scope;
    }

    public enum MessageScope {
        PRIVATE,
        PUBLIC
    }
}
