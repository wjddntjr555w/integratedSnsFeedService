package dayonglee.SnsFeedService.repository;

import dayonglee.SnsFeedService.domain.Feed;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Optional<Feed> findByFeedId(String feedId);

    Optional<List<Feed>> findFeedsByFeedOwner(Long feedOwner);

    Optional<List<Feed>> findFeedsByCreatedTimeBetween(Date start, Date end, Sort sort);
}
