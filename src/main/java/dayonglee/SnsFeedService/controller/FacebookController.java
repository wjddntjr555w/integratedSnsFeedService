package dayonglee.SnsFeedService.controller;


import dayonglee.SnsFeedService.service.FacebookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FacebookController {

    private final FacebookService facebookService;

    @GetMapping("sns/authorization/facebook")
    public String authFacebook(RedirectAttributes redirectAttributes){


        Map<String,String> map = facebookService.queryReturn();
        log.info("map = {}",map);
        redirectAttributes.addAllAttributes(map);
        log.info("redirectAttributes = {}",redirectAttributes);

        return "redirect:https://www.facebook.com/v13.0/dialog/oauth";
    }

    // refresh post가 맞지 않을까 하는 생각?
    // 왜 why, get은 리소스를 찾는건데, post는 뭔가의 리소스에 대한 요청이기때문에
    @GetMapping("sns/refresh/facebook")
    public String refreshFacebook(RedirectAttributes redirectAttributes){
        facebookService.feedRefresh();

        redirectAttributes.addFlashAttribute("facebookRefresh","success");
        return "redirect:/sns/snsLogin";
    }
}
