package dayonglee.SnsFeedService.service;

import dayonglee.SnsFeedService.domain.Feed;
import dayonglee.SnsFeedService.repository.FeedRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Service
public class FacebookService {

    private final FeedRepository feedRepository;
    private final HttpSession httpSession;

    @Getter
    private Boolean facebookLoginCheck = false;

    private String string;
    private String at;
    private String ui;

    @Value("${facebook.security.info.client_id}")
    String client_id;

    @Value("${facebook.security.info.client_secret}")
    String client_secret;

    @Value("${facebook.security.info.scope}")
    String scope;

    @Value("${facebook.security.info.redirect_uri}")
    String redirect_uri;

    @Value("${facebook.security.info.state}")
    String state;

    public void getUser(){
        String uri = "https://www.facebook.com/v13.0/dialog/oauth";
        WebClient webClient = WebClient.create(uri);
        Mono<String> dataMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("client_id",client_id)
                        .queryParam("redirect_uri",redirect_uri)
                        .queryParam("state",state)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public void feedRefresh(){
        getFeed(at,ui);
    }
    public void getFeed(String access_token,String userId) {
        WebClient webClient = WebClient.create();
        String uri = "https://graph.facebook.com/v13.0/" + userId + "/feed";
        Mono<String> dataMono = webClient.get()
                .uri(uri)
                .headers(h -> h.setBearerAuth(access_token))
                .retrieve()
                .bodyToMono(String.class);
        string = dataMono.block();
        facebookFeedSaveOrUpdate();
    }

    public void facebookFeedSaveOrUpdate() {

        JSONObject jsonObject = new JSONObject(string);
        JSONArray data = (JSONArray) jsonObject.get("data");
        Long owner = (Long) httpSession.getAttribute("owner");
        for (int i=0; i<data.length();i++) {
//            log.info("id = {}", data.getJSONObject(i).get("id"));

            org.json.JSONObject attributes = data.getJSONObject(i);
            Feed newFeed;
            if (attributes.has("message")) { // feed가 비공개일 경우는 받지 않음(id는 받아지나 피드 내용을 받을 수 없음)
                newFeed = new Feed(null, (String) attributes.get("created_time"), (String) attributes.get("message"), (String) attributes.get("id"),owner);
                Feed feed = feedRepository.findByFeedId((String) attributes.get("id"))
                        .map(entity -> entity.update((String)attributes.get("message"),
                                (String)attributes.get("created_time"),
                                (String)attributes.get("id"),
                                (Long) owner))
                        .orElse(newFeed);
//                log.info("feed = {}", feed);
                feedRepository.save(feed);
            }
        }
    }

    public String getUserId(String access_token){
        String uri = "https://graph.facebook.com/me";
        WebClient webClient = WebClient.create(uri);
        Mono<String> dataMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("access_token",access_token)
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        String s = dataMono.block();
        JSONObject jsonObject = new JSONObject(s);
        String userId = String.valueOf(jsonObject.get("id"));

        return userId;
    }

    public String codeToToken(String code) {
        String uri = "https://graph.facebook.com/v13.0/oauth/access_token";
        WebClient webClient = WebClient.create(uri);
        Mono<String> dataMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("client_id",client_id)
                        .queryParam("redirect_uri",redirect_uri)
                        .queryParam("client_secret",client_secret)
                        .queryParam("code",code)
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        String s = dataMono.block();
        JSONObject jsonObject = new JSONObject(s);
        String access_token = String.valueOf(jsonObject.get("access_token"));
//        log.info("facebook access_token={}",access_token);
        httpSession.setAttribute("facebook", "login");
        facebookLoginCheck = true;
        //user id 획득
        String userId= getUserId(access_token);
//        log.info("facebook userId={}",userId);

        //user id로 피드 획득
        at = access_token;
        ui = userId;
        getFeed(access_token,userId);

        return access_token;
    }

    public Map<String,String> queryReturn() {
        Map<String,String> map = new HashMap<>();
        map.put("client_id", client_id);
        map.put("redirect_uri", redirect_uri);
        map.put("state", state);
        return map;
    }

    public void feedRefreshScheduler() {
        getFeed(at,ui);
    }
}
