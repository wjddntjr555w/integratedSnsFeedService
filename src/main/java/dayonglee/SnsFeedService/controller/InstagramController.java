package dayonglee.SnsFeedService.controller;

import dayonglee.SnsFeedService.service.InstagramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InstagramController {

    private final InstagramService instagramService;

    @GetMapping("sns/authorization/instagram")
    public String authInstagram(RedirectAttributes redirectAttributes){

        Map<String,String> map = instagramService.queryReturn();
        log.info("map = {}",map);
        redirectAttributes.addAllAttributes(map);
        log.info("redirectAttributes = {}",redirectAttributes);

        return "redirect:https://api.instagram.com/oauth/authorize";
    }

    @GetMapping("instagram/feed")
    public String instagramFeed(Model model){
        model.addAttribute("feeds","feeds");
        return "instagramFeed";
    }

    @GetMapping("sns/refresh/instagram")
    public String refreshInstagram(RedirectAttributes redirectAttributes){
        instagramService.feedRefresh();
        redirectAttributes.addFlashAttribute("instagramRefresh","success");

        return "redirect:/sns/snsLogin";
    }
}

