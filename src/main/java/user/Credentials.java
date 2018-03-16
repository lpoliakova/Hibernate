package user;

import util.UserNameConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

/**
 * Created by oradchykova on 11/7/17.
 */
@Embeddable
public class Credentials {
    @Column(nullable = false, unique = true, updatable = false)
    private String login;

    @Convert(converter = UserNameConverter.class)
    @Column(name = "user_name", nullable = false)
    private UserName name;

    protected Credentials() {

    }

    public Credentials(String login, UserName userName) {
        this.login = login;
        this.name = userName;
    }

    public String getLogin() {
        return login;
    }

    public UserName getName() {
        return name;
    }

    void setName(UserName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credentials that = (Credentials) o;

        return login.equals(that.login);
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
