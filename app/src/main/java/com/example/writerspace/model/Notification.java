package com.example.writerspace.model;

public class Notification {
    private String userid;
    private String text;
    private String postid;
    private String title;
    private boolean image;
    private boolean writing;
    private boolean audio;

    public Notification(String userid, String text, String postid, String title, boolean image, boolean writing, boolean audio) {
        this.userid = userid;
        this.text = text;
        this.postid = postid;
        this.title = title;
        this.image = image;
        this.writing = writing;
        this.audio = audio;
    }

    public Notification() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public boolean isWriting() {
        return writing;
    }

    public void setWriting(boolean writing) {
        this.writing = writing;
    }

    public boolean isAudio() {
        return audio;
    }

    public void setAudio(boolean audio) {
        this.audio = audio;
    }
}
