package entities;

import values.Credentials;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Created by oradchykova on 11/8/17.
 */
@Entity
@PrimaryKeyJoinColumn(name = "fk_users")
public class SecuredUser extends User{

    @Column(nullable = false)
    private String password;

    protected SecuredUser() {
    }

    public SecuredUser(Credentials credentials, String password) {
        super(credentials);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
