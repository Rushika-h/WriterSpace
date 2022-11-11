package com.example.writerspace.model;

public class post_audio {
    String audiofile;
    String audiotitle;
    String description;
    String postid;
    String publisher;

    public post_audio(String audiofile, String audiotitle, String description, String postid, String publisher) {
        this.audiofile = audiofile;
        this.audiotitle = audiotitle;
        this.description = description;
        this.postid = postid;
        this.publisher = publisher;
    }

    public post_audio() {
    }

    public String getAudiofile() {
        return audiofile;
    }

    public void setAudiofile(String audiofile) {
        this.audiofile = audiofile;
    }

    public String getAudiotitle() {
        return audiotitle;
    }

    public void setAudiotitle(String audiotitle) {
        this.audiotitle = audiotitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
