package com.aarafrao.busterlord_hiringscheduler.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "context")
    private String context;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "time")
    private String time;


    Notification(int id, String title, String message, String context) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.context = context;
    }

    @Ignore
    public Notification(String title, String message, String context, String time) {
        this.title = title;
        this.message = message;
        this.context = context;
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
