package com.example.diarycalendar.bean;

public class ItemInfos {
    private String date;
    private String title;
    private String content;

    public ItemInfos(String date, String title, String content) {
        this.date = date;
        this.title = title;
        this.content = content;
    }

    public void setDate(String date) { this.date = date; }

    public void setTitle(String title) { this.title = title; }

    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }

    public String getTitle() { return title; }

    public String getContent() { return content; }
}
