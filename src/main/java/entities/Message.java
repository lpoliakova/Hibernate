package entities;

import com.sun.istack.internal.NotNull;

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

    private String text;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY,
        optional = false)
    @JoinColumn(
            name = "user_id",
            updatable = false)
    private User creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date creationTimestamp;

    protected Message() {

    }

    public Message(String text, User creator) {
        this.text = text;
        this.creator = creator;
    }

    public String getText() {
        return text;
    }

    public User getCreator() {
        return creator;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }
}
