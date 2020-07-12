package controller;

public enum LocationManager {

    INSTANCE;

    private Integer topicID;
    private Integer threadID;
    private Integer noteID;

    private LocationManager() {
    }

    public Integer getTopicID() {
        return topicID;
    }

    public void setTopicID(Integer topicID) {
        this.topicID = topicID;
    }

    public Integer getThreadID() {
        return threadID;
    }

    public void setThreadID(Integer threadID) {
        this.threadID = threadID;
    }

    public Integer getNoteID() {
        return noteID;
    }

    public void setNoteID(Integer noteID) {
        this.noteID = noteID;
    }
}
