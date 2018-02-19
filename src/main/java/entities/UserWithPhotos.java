package entities;

import values.Credentials;
import values.Image;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@PrimaryKeyJoinColumn(name = "fk_users")
public class UserWithPhotos extends User {

    @ElementCollection
    @MapKeyColumn(name = "photo_name")
    @JoinColumn(name = "fk_users")
    private Map<String, Image> photos = new HashMap<>();

    protected UserWithPhotos() {
    }

    public UserWithPhotos(Credentials credentials) {
        super(credentials);
    }

    public Map<String, Image> getPhotos() {
        return Collections.unmodifiableMap(photos);
    }

    public void addPhoto(String name, Image photo) {
        this.photos.put(name, photo);
    }

    public void removePhoto(String name) {
        this.photos.remove(name);
    }
}
