package model;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public abstract class Section{

    private int id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Section() {};

    public Section(String title) {
        this.title = title;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedAt(long savedTimestamp) {
        Timestamp timestamp = new Timestamp(savedTimestamp);
        this.createdAt = timestamp.toLocalDateTime();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(long savedTimestamp) {
        Timestamp timestamp = new Timestamp(savedTimestamp);
        this.updatedAt = timestamp.toLocalDateTime();
    }

}
