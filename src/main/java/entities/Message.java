package entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by oradchykova on 7/8/17.
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY,
        optional = false)
    @JoinColumn(
            name = "fk_users",
            updatable = false)
    private User creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_dtm", updatable = false) //TODO: nullable = false
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
