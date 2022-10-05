package dayonglee.SnsFeedService.service;

import dayonglee.SnsFeedService.domain.Feed;
import dayonglee.SnsFeedService.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    public List<Feed> getFeedsBetweenStartDateAndEndDate(Date start, Date end) {

        List<Feed> feedsResult = new ArrayList<>();
        // 옵셔널이기 때문에, 실제 조회가 성공했는지 실패했는지 모른다.
        Sort sort = Sort.by("createdTime").descending();
        Optional<List<Feed>> findedFeeds = feedRepository.findFeedsByCreatedTimeBetween(start, end,sort);



        if (findedFeeds.isPresent()) {

            feedsResult = findedFeeds.get();
        }
        return feedsResult;


    }
}
