package com.example.writerspace.model;

public class post_text {
    private String postid;
    private String publisher;
    private String text;
    private String description;
    private String title;

    public post_text(String postid, String publisher, String text, String description, String title) {
        this.postid = postid;
        this.publisher = publisher;
        this.text = text;
        this.description = description;
        this.title = title;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public post_text() {
    }
}
