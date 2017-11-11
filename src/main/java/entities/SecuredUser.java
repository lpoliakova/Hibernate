package entities;

import values.Credentials;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Created by oradchykova on 11/8/17.
 */
@Entity
@PrimaryKeyJoinColumn(name = "SECURED_USER")
public class SecuredUser extends User{
    private String password;

    public SecuredUser() {
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
