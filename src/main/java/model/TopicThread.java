package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class TopicThread extends Section implements Retrievable{

    private Integer topicID;

    public TopicThread(String title) {
       super(title);
    }

    public TopicThread(Map<String, Object> dbo) {
        this.restoreFromDbo(dbo);
    }

    public Integer getTopicID() {
        return topicID;
    }

    public void setTopicID(Integer topicID) {
        this.topicID = topicID;
    }

    @Override
    public void restoreFromDbo(Map<String, Object> dbo) {
        this.setId(
                (Integer)dbo.get("threadID")
        );
        this.setTitle(
                (String)dbo.get("title")
        );
        this.setCreatedAt(
                (long)dbo.get("timeCreated")
        );
        this.setUpdatedAt(
                (long)dbo.get("lastUpdated")
        );

        this.setTopicID(
                (Integer)dbo.get("topicID")
        );
    }
}
