package dayonglee.SnsFeedService.repository;

import dayonglee.SnsFeedService.domain.Feed;
import dayonglee.SnsFeedService.domain.InstaFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstaFeedRepository extends JpaRepository<InstaFeed,Long> {

    Optional<Feed> findByFeedId(String feedId);

}
