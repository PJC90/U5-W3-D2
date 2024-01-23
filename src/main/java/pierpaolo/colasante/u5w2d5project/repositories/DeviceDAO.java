package pierpaolo.colasante.u5w2d5project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pierpaolo.colasante.u5w2d5project.entities.Device;

import java.util.Optional;

@Repository
public interface DeviceDAO extends JpaRepository<Device, Long> {

}
