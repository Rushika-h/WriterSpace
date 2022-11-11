package com.example.writerspace.model;

import com.google.firebase.auth.FirebaseAuth;

public class user {
    public String id;
    public String uname;
    public String email;
    public String imageurl;
    public String bio;
    public String password;
    public String fullname;

    public user(String id, String uname, String email, String imageurl, String bio, String password, String fullname) {
        this.id = id;
        this.uname = uname;
        this.email = email;
        this.imageurl = imageurl;
        this.bio = bio;
        this.password = password;
        this.fullname = fullname;
    }

    public user() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /*public user(String id, String uname, String imageurl, String bio, String email,String password) {
        this.id = id;
        this.uname = uname;
        this.email = email;
        this.imageurl = imageurl;
        this.bio = bio;
        this.password=password;
    }

    public user() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
*/}
