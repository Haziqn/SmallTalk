package com.example.a15017523.smalltalk;

/**
 * Created by 15017523 on 16/6/2017.
 */

public class Users {
    private String name;
    private String photoUrl;

    public Users () {}

    public Users(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
