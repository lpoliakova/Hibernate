package user;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "users_with_photos")
@PrimaryKeyJoinColumn(
        name = "fk_users",
        referencedColumnName = "user_id",
        foreignKey = @ForeignKey(name = "fk_users_with_photos_users_user_id"))
public class UserWithPhotos extends User {

    @ElementCollection
    @CollectionTable(name="photos")
    @MapKeyColumn(name = "photo_name")
    @JoinColumn(name = "fk_users", referencedColumnName = "fk_users")
    @org.hibernate.annotations.ForeignKey(name = "photos_users_with_photos_fk_users")
    private Map<String, Image> photos = new HashMap<>();

    protected UserWithPhotos() {
    }

    public UserWithPhotos(Credentials credentials) {
        super(credentials);
    }

    public Map<String, Image> getPhotos() {
        return Collections.unmodifiableMap(photos);
    }

    void addPhoto(String name, Image photo) {
        this.photos.put(name, photo);
    }

    void removePhoto(String name) {
        this.photos.remove(name);
    }
}
