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
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="created_time")
    private Date createdTime;
    @Column(name = "message", length = 2500)
    private String message;
    @Column(name="feed_id")
    private String feedId;

    @Column(name="feed_owner")
    private Long feedOwner;

    @Builder
    public Feed(Long id,String createdTime, String message, String feedId, Long feedOwner) {
        this.id = id;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.createdTime = new Date(sdf.parse(createdTime).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.message = message;
        this.feedId = feedId;
        this.feedOwner = feedOwner;
    }

    public Feed update(String message, String createdTime, String feedId, Long feedOwner) {
        this.message = message;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.createdTime = new Date(sdf.parse(createdTime).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.feedId = feedId;
        this.feedOwner = feedOwner;
        return this;
    }

}
