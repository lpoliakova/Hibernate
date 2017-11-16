import entities.Device;
import entities.User;
import org.junit.Test;
import values.Credentials;
import values.UserName;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by oradchykova on 11/16/17.
 */
public class DeviceTest {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("myTest");

    @Test
    public void deviceTest() {
        EntityManager em = emf.createEntityManager();

        Device device = new Device(Device.DeviceType.TABLET, "iPad 2");

        try {
            em.getTransaction().begin();

            em.persist(device);

            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        List<Device> devices;

        try {
            em.getTransaction().begin();

            devices = em.createQuery("select d from Device d", Device.class).getResultList();

            em.getTransaction().commit();
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }

        assertEquals(1, devices.size());
        assertNull(devices.get(0).getUser());
        assertEquals(device.getType(), devices.get(0).getType());
        assertEquals(device.getModel(), devices.get(0).getModel());
    }

    @Test
    public void userDeviceTest() {
        EntityManager em = emf.createEntityManager();

        Credentials sashasCredentials = new Credentials("sashen'ka", new UserName("Oleksandr", "Radchykov"));
        User userSasha = new User(sashasCredentials);
        Device device = new Device(Device.DeviceType.TABLET, "iPad 2");
        userSasha.setDevice(device);

        try {
            em.getTransaction().begin();

            em.persist(userSasha);

            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }

        User foundUser = em.find(User.class, userSasha.getId());

    }
}
