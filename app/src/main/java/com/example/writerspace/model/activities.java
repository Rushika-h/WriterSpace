package com.example.writerspace.model;

import java.util.Map;

public class activities {
    private String userid;
    private String activityid;
    private String activitytype;
    private String postid;
    private long timestamp;

    public activities(String userid, String activityid, String activitytype, String postid, long timestamp) {
        this.userid = userid;
        this.activityid = activityid;
        this.activitytype = activitytype;
        this.postid = postid;
        this.timestamp = timestamp;
    }

    public activities() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getActivityid() {
        return activityid;
    }

    public void setActivityid(String activityid) {
        this.activityid = activityid;
    }

    public String getActivitytype() {
        return activitytype;
    }

    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
