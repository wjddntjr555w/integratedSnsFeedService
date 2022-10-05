package dayonglee.SnsFeedService.config;

import dayonglee.SnsFeedService.repository.MainUserRepository;
import dayonglee.SnsFeedService.service.FacebookService;
import dayonglee.SnsFeedService.service.InstagramService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.awt.print.Pageable;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class SchedulingConfig {

    // 예시, 유저 카운트 구함.

    @Autowired
    private final FacebookService facebookService;
    @Autowired
    private final InstagramService instagramService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    // fixedRate = 주기
    @Scheduled(fixedRate = 60000)
    public void userFeedUpdateTasks() {
        log.info("scheduler call success");

        // 엑세스토큰 없으면 안돌게 하고 있으면 돌게

        Boolean instagramLoginCheck = instagramService.getInstagramLoginCheck();
        Boolean facebookLoginCheck = facebookService.getFacebookLoginCheck();

        if (facebookLoginCheck && instagramLoginCheck){ //고민 더 해봐야 하는 부분..

            try{
                log.info("scheduler facebook call success");
                facebookService.feedRefreshScheduler();
            }catch (Exception e){
                log.info("Exception = ", e);
            } finally {
                log.info("scheduler instagram call success");
                instagramService.feedRefreshScheduler();
            }

        }

        else if(facebookLoginCheck) {
            log.info("scheduler facebook call success");
            // get Facebook Feed
            facebookService.feedRefreshScheduler();
        }

        else if (instagramLoginCheck) {
            log.info("scheduler instagram call success");
            // get Instagram Feed
            instagramService.feedRefreshScheduler();
        }

    }
}