package user;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by oradchykova on 11/8/17.
 */
@Entity
@Table(name = "users_with_emails")
@PrimaryKeyJoinColumn(
        name = "fk_users",
        referencedColumnName = "user_id",
        foreignKey = @ForeignKey(name = "fk_users_with_emails_users_user_id"))
public class UserWithEmail extends User {

    @ElementCollection
    @CollectionTable(name = "emails")
    @Column(name = "email", nullable = false)
    @JoinColumn(name = "fk_users", referencedColumnName = "fk_users")
    @org.hibernate.annotations.ForeignKey(name = "emails_users_with_emails_fk_users")
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
}
