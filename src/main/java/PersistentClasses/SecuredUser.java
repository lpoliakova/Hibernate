package PersistentClasses;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by oradchykova on 11/8/17.
 */
@Entity
@DiscriminatorValue("SU")
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
