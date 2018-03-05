package entities;

import values.Credentials;

import javax.persistence.*;

/**
 * Created by oradchykova on 11/8/17.
 */
@Entity
@Table(name = "secured_users")
@PrimaryKeyJoinColumn(
        name = "fk_users",
        foreignKey = @ForeignKey(name = "fk_secured_users_users_user_id"))
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
