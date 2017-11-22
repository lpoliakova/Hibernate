package entities;

import values.Credentials;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by oradchykova on 11/8/17.
 */
@Entity
@PrimaryKeyJoinColumn(name = "fk_users")
public class UserWithEmail extends User {
    @ElementCollection
    @Column(name = "email", nullable = false)
    @JoinColumn(name = "fk_users")
    @org.hibernate.annotations.OrderBy(clause = "email desc")
    private Set<String> emails = new LinkedHashSet<>();

    protected UserWithEmail() {
    }

    public UserWithEmail(Credentials credentials) {
        super(credentials);
    }

    public Set<String> getEmails() {
        return Collections.unmodifiableSet(emails);
    }

    public void addEmail(String email) {
        this.emails.add(email);
    }

    public void removeEmail(String email) {
        this.emails.remove(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserWithEmail that = (UserWithEmail) o;

        return emails.equals(that.emails);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + emails.hashCode();
        return result;
    }
}
