package PersistentClasses;

import Util.UserNameConverter;
import com.sun.istack.internal.NotNull;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

/**
 * Created by oradchykova on 11/7/17.
 */
@Embeddable
public class Credentials {
    @Column(name = "LOGIN", nullable = false)
    String login;

    @NotNull
    @Convert(converter = UserNameConverter.class)
    @Column(name = "USER_NAME")
    UserName name;

    public Credentials() {

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
}
