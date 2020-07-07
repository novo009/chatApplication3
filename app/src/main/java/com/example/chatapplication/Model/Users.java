package com.example.chatapplication.Model;

public class Users {
    private String id;
    private String username;
    private  String ImageUrl;

    public Users(){

    }
    public String getId() {
        return id;
    }

    public Users(String id, String username, String imageUrl) {
        this.id = id;
        this.username = username;
        this.ImageUrl = imageUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }
}
