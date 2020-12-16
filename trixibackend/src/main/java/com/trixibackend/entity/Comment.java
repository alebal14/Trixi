package com.trixibackend.entity;

import org.bson.types.ObjectId;

public class Comment {

    private ObjectId id;
    private String comment;
    private String postId;
    private String userId;

    public Comment(){}

    public Comment(String comment) {
        this.comment = comment;
    }

    public Comment(String comment, String postId, String userId) {
        this.comment = comment;
        this.postId = postId;
        this.userId = userId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", postId='" + postId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
