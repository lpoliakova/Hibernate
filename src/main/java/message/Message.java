package message;

import group.UserInGroup;

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
    @JoinColumns(foreignKey = @ForeignKey(name = "fk_messages_groups_users_group_id_user_id"),
            value = {
            @JoinColumn(
                    name = "fk_groups",
                    referencedColumnName = "fk_groups",
                    updatable = false),
            @JoinColumn(
                    name = "fk_users",
                    referencedColumnName = "fk_users",
                    updatable = false)
    })
    private UserInGroup place;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_dtm", updatable = false) //TODO: nullable = false
    @org.hibernate.annotations.CreationTimestamp
    private Date creationTimestamp;

    protected Message() {

    }

    public Message(String text, UserInGroup place) {
        this.text = text;
        this.place = place;
    }

    public String getText() {
        return text;
    }

    public UserInGroup getPlace() {
        return place;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }
}
