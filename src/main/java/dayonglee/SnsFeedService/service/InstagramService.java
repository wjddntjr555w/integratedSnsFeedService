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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Service
public class InstagramService {

    private final FeedRepository feedRepository;
    private final HttpSession httpSession;

    @Getter
    private Boolean instagramLoginCheck = false;
    private String string;
    private String at;
    private String ui;

    @Value("${instagram.security.info.client_id}")
    String client_id;

    @Value("${instagram.security.info.client_secret}")
    String client_secret;

    @Value("${instagram.security.info.redirect_uri}")
    String redirect_uri;

    @Value("${instagram.security.info.scope}")
    String scope;

    @Value("${instagram.security.info.response_type}")
    String response_type;


    public Map<String, String> queryReturn(){
        Map<String,String> map = new HashMap<>();
        map.put("client_id", client_id);
        map.put("redirect_uri", redirect_uri);
        map.put("scope", scope);
        map.put("response_type", response_type);

        return map;
    }

    public String codeToToken(String code){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id",client_id );
        params.add("client_secret", client_secret);
        params.add("grant_type","authorization_code" );
        params.add("redirect_uri",redirect_uri);
        params.add("code", code);

        WebClient webClient = WebClient.create();
        String uri = "https://api.instagram.com/oauth/access_token";
        Mono<String> dataMono = webClient.post()
                .uri(uri)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(String.class);

        String s = dataMono.block();
        JSONObject jsonObject = new JSONObject(s);
        String access_token = String.valueOf(jsonObject.get("access_token"));
        String userId = String.valueOf(jsonObject.get("user_id"));

        httpSession.setAttribute("instagram", "login");
        instagramLoginCheck = true;

        //미디어 아이디 획득
        Mono<String> mediaIds = getMediaId(access_token,userId);
        s = mediaIds.block();
        JSONObject mediaObject = new JSONObject(s);
        JSONArray mediaIds2 = (JSONArray) mediaObject.get("data");

        //미디어 아이디로 피드 푁득
        for(int i=0;i<mediaIds2.length();i++){
            JSONObject tmp = (JSONObject)mediaIds2.get(i);
            String id = (String) tmp.get("id");
            at = access_token;
            ui = id;
            Mono<String> dataMonoFeed = getMediaFeed(access_token,id);
            string = dataMonoFeed.block();
            instagramFeedSaveOrUpdate();
        }
//        mediaIds.subscribe(s1 -> log.info("mediaIds = {}",s1));


//
        return s;
    }

    public void feedRefresh(){
        getMediaFeed(at,ui);
    }


    private Mono<String> getMediaId(String access_token, String userId) {
        String uriFeed = "https://graph.instagram.com/"+userId+"/media";
        WebClient webClient = WebClient.create(uriFeed);
        Mono<String> dataMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
//                        .queryParam("fields","id")
                        .queryParam("access_token", access_token)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
        return dataMono;
    }

    private Mono<String> getMediaFeed(String access_token, String mediaId) {
        String uriFeed = "https://graph.instagram.com/"+mediaId;
        WebClient webClient = WebClient.create(uriFeed);
        Mono<String> dataMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("fields","id,caption,timestamp")
                        .queryParam("access_token", access_token)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
        return dataMono;
    }

    public void instagramFeedSaveOrUpdate(){

        JSONObject attributes = new JSONObject(string);
        Long owner = (Long) httpSession.getAttribute("owner");
        Feed newFeed = new Feed(null, (String) attributes.get("timestamp"), (String) attributes.get("caption"), (String) attributes.get("id"), (Long) owner);
        Feed feed = feedRepository.findByFeedId((String) attributes.get("id"))
                .map(entity -> entity.update((String)attributes.get("caption"),
                        (String)attributes.get("timestamp"),
                        (String)attributes.get("id"),
                        (Long) owner))
                .orElse(newFeed);
//        log.info("feed = {}", feed);
        feedRepository.save(feed);
    }

    public void feedRefreshScheduler() {
        getMediaFeed(at,ui);
    }
}

