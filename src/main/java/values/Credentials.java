package values;

import util.UserNameConverter;
import com.sun.istack.internal.NotNull;
import values.UserName;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

/**
 * Created by oradchykova on 11/7/17.
 */
@Embeddable
public class Credentials {
    @Column(nullable = false, unique = true)
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

    public void setLogin(String login) {
        this.login = login;
    }

    public UserName getName() {
        return name;
    }

    public void setName(UserName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credentials that = (Credentials) o;

        if (!login.equals(that.login)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
