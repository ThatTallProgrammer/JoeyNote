package model;

import java.sql.Timestamp;
import java.util.Map;

public class Note extends Section implements Retrievable{

    private Integer threadID;
    private String directory = ""; // what directory was the note created in
    private String body = "";

    public Note(String title) {
       super(title);
    }

    public Note(Map<String, Object> dbo) {
        this.restoreFromDbo(dbo);
    }

    public Integer getThreadID() {
        return threadID;
    }

    public void setThreadID(Integer threadID) {
        this.threadID = threadID;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public void restoreFromDbo(Map<String, Object> dbo) {
        this.setId(
                (Integer)dbo.get("noteID")
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

        this.setThreadID(
                (Integer)dbo.get("threadID")
        );
        this.setDirectory(
                (String)dbo.get("directory")
        );
        this.setBody(
                (String)dbo.get("body")
        );
    }
}
