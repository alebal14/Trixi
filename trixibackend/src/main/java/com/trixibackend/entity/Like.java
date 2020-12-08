package com.trixibackend.entity;

import org.bson.types.ObjectId;

public class Like {
    private ObjectId id;
    private String userId;
    private String postId;


    public Like(){

    }

    public Like(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
