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
@PrimaryKeyJoinColumn(name = "USER_WITH_EMAIL")
public class UserWithEmail extends User {
    @ElementCollection
    @CollectionTable(name = "EMAILS")
    @Column(name = "EMAIL")
    @org.hibernate.annotations.OrderBy(clause = "EMAIL desc")
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
}
