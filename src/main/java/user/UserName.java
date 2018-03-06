package user;

import java.io.Serializable;

/**
 * Created by oradchykova on 11/7/17.
 */
public class UserName implements Serializable{
    String firstName;
    String lastName;

    public UserName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String toString() {
        return firstName + " " + lastName;
    }

    public static UserName fromString(String source) {
        String[] parts = source.split(" ");
        return new UserName(parts[0], parts[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserName userName = (UserName) o;

        if (!firstName.equals(userName.firstName)) return false;
        return lastName.equals(userName.lastName);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }
}
