package dayonglee.SnsFeedService.repository;


import dayonglee.SnsFeedService.domain.MainUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MainUserRepository extends JpaRepository<MainUser,Long> {

    Optional<MainUser> findByEmail(String email);
}
