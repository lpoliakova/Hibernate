package values;

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
}
