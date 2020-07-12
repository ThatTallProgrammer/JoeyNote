package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class Topic extends Section implements Retrievable {

    public Topic(String title) {
        super(title);
    }

    public Topic(Map<String, Object> dbo) {
        this.restoreFromDbo(dbo);
    }

    @Override
    public String toString() {
        String title = this.getTitle();
        return String.format("%-25s%-30s%-30s",
                title.length() <= 20 ? title : title.substring(0, 20) + "...",
                this.getCreatedAt(),
                this.getUpdatedAt()
        );
    }

    @Override
    public void restoreFromDbo(Map<String, Object> dbo) {
        this.setId(
                (Integer)dbo.get("topicID")
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
    }
}
