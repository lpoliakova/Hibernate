package entities;

import javax.persistence.*;

/**
 * Created by oradchykova on 11/16/17.
 */
@Entity
public class Device {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeviceType type;
    private String model;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_DEVICE",
            joinColumns = @JoinColumn(name = "DEVICE_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", unique = true)
    )
    private User user;

    protected Device() {
    }

    public Device(DeviceType type, String model) {
        this.type = type;
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum DeviceType {
        SMART_PHONE,
        TABLET,
        LAPTOP
    }
}
