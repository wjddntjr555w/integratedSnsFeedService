package dayonglee.SnsFeedService.controller;

import dayonglee.SnsFeedService.service.FacebookService;
import dayonglee.SnsFeedService.service.InstagramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/sns")
public class SnsIndexController {

    private final InstagramService instagramService;
    private final FacebookService facebookService;
    private final HttpSession httpSession;

    @GetMapping("/snsLogin")
    public String snsLogin(@RequestParam(value = "code", required = false) String code,
                           @RequestParam(value = "state", required = false) String state,
                           Model model) {

        String instagram = (String) httpSession.getAttribute("instagram");
        String facebook = (String) httpSession.getAttribute("facebook");

        if (code != null) {
            if(state != null){
                String token = facebookService.codeToToken(code);
                model.addAttribute("facebook", "login");
            }
            else {
                String token = instagramService.codeToToken(code);
                JSONObject jsonObject = new JSONObject(token);
                model.addAttribute("instagram", "login");
            }
        }

        if (instagram != null) {
            model.addAttribute("instagram", "login");
        }
        if (facebook != null){
            model.addAttribute("facebook", "login");
        }

        return "sns/snsLogin";


    }

}