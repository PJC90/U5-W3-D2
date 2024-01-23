package pierpaolo.colasante.u5w2d5project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pierpaolo.colasante.u5w2d5project.entities.User;

import java.util.Optional;

@Repository
public interface UserDAO  extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

}
