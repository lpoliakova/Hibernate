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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SecuredUser that = (SecuredUser) o;

        return password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
