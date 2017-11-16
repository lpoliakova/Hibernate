package entities;

import values.Credentials;

import javax.persistence.*;

/**
 * Created by oradchykova on 8/21/17.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "USER_TYPE")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Credentials credentials;

    @OneToOne (
            fetch = FetchType.LAZY,
            /*optional = false,*/
            cascade = CascadeType.PERSIST
    )
    private Device device;

    @Column(name = "PRIORITY")
    @org.hibernate.annotations.ColumnTransformer(
            forColumn = "PRIORITY",
            read = "PRIORITY - 1",
            write = "? + 1"
    )
    private int priority;

    protected User(){}

    public User(Credentials credentials){
        this.credentials = credentials;
        priority = 10;
    }

    public Long getId() {
        return id;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
