package dayonglee.SnsFeedService.controller;

import dayonglee.SnsFeedService.domain.Feed;
import dayonglee.SnsFeedService.repository.FeedRepository;
import dayonglee.SnsFeedService.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
public class FeedController {

    private final HttpSession httpSession;

    @Autowired
    private final FeedRepository feedRepository;

    @Autowired
    private FeedService feedService;

    @GetMapping("feedIndex")
    public String facebookFeed(Model model){

        Sort sort1 = Sort.by("createdTime").descending();
        Pageable pageable = PageRequest.of(0,100, sort1);
        Long owner = (Long) httpSession.getAttribute("owner");

        Page<Feed> result = feedService.getFeed(pageable);
        // 컨트롤러에서 바로 Repository에 접근..하면 안된다...
//        Page<Feed> result = feedRepository.findAll(pageable);

        model.addAttribute("feeds",result);
        return "feedIndex";
    }

    @PostMapping("feedIndex")
    public String facebookFeed2(@RequestParam("feed-start") String feedStart, @RequestParam("feed-end") String feedEnd,Model model){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date start = null;
        try {
            start = new Date(sdf.parse(feedStart).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Date end = null;
        try {
            end = new Date(sdf.parse(feedEnd).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<Feed> result = feedService.getFeedsBetweenStartDateAndEndDate(start, end);

        model.addAttribute("feeds", result);

        return "feedIndex";
    }
}
