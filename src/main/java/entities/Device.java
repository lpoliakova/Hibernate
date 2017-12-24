package entities;

import javax.persistence.*;

/**
 * Created by oradchykova on 11/16/17.
 */
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue
    @Column(name = "device_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type",
            nullable = false, updatable = false)
    private DeviceType type;

    @Column(nullable = false, updatable = false)
    private String model;

    @OneToOne(fetch = FetchType.LAZY,
        mappedBy = "device")
    @JoinTable(
            name = "user_device",
            joinColumns = @JoinColumn(name = "fk_device"),
            inverseJoinColumns = @JoinColumn(name = "fk_user", unique = true)
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

    public String getModel() {
        return model;
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
