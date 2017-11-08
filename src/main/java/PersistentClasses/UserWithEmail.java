package PersistentClasses;

import javax.persistence.*;

/**
 * Created by oradchykova on 11/8/17.
 */
@Entity
@DiscriminatorValue("EU")
public class UserWithEmail extends User {
    private String email;

    public UserWithEmail() {
    }

    public UserWithEmail(Credentials credentials, String email) {
        super(credentials);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
