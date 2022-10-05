package dayonglee.SnsFeedService.controller;


import dayonglee.SnsFeedService.util.JwtTokenProvider;
import dayonglee.SnsFeedService.dto.LoginDto;
import dayonglee.SnsFeedService.domain.MainUser;
import dayonglee.SnsFeedService.repository.MainUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainIndexController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MainUserRepository mainUserRepository;
    private final HttpSession httpSession;

    // 메인 로그인 화면 출력
    @GetMapping("/")
    public String loginPage(){
        return "mainLogin";
    }


    // 로그인
    @PostMapping("/")
    public String login(@ModelAttribute("user") LoginDto mainUser, Model model, HttpServletResponse response) {

        if (mainUserRepository.findByEmail(mainUser.getEmail()).isEmpty()){
            model.addAttribute("loginError","loginError");
            return "mainLogin";
        }
        MainUser member = mainUserRepository.findByEmail(mainUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(mainUser.getPassword(), member.getPassword())) {
            model.addAttribute("loginError","loginError");
            return "mainLogin";
        }
        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
        Cookie cookie = new Cookie("X-AUTH-TOKEN",token);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        response.setHeader("X-AUTH-TOKEN",token);

        httpSession.setAttribute("owner",member.getId());
        httpSession.setAttribute("X-AUTH-TOKEN",token);


        return "redirect:/sns/snsLogin";
    }

    // 메인 로그인 화면 출력
    @GetMapping("/join")
    public String joinPage(){
        return "mainJoinUser";
    }

    // 회원가입
    @PostMapping("/join")
    public String join(@RequestParam("email") String email,@RequestParam("password") String password) {
        log.info("user = {} {}",email,password);
        Long id = mainUserRepository.save(MainUser.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getId();
        return "redirect:/";
    }


}
