package dayonglee.SnsFeedService.domain;

import lombok.*;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class InstaFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="created_time")
    private Date createdTime;

    @Column(name="caption", length = 2500)
    private String caption;

    @Column(name="feed_id")
    private String feedId;

    @Builder
    public InstaFeed(Long id,String createdTime, String caption, String feedId) {
        this.id = id;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        try {
            this.createdTime = new Date(sdf.parse(createdTime).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.caption = caption;
        this.feedId = feedId;
    }

    public InstaFeed update(String caption, String createdTime, String feedId) {
        this.caption = caption;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        try {
            this.createdTime = new Date(sdf.parse(createdTime).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.feedId = feedId;

        return this;
    }
}
