package com.trixibackend.entity;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private ObjectId id;
    private String uid;
    private String title;
    private String description;
    private String filePath;

    private String ownerId;
    private String categoryId;

    private List<Comment> comments = new ArrayList<>();
    private List<Like> likes = new ArrayList<>();

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Post() {

    }

    public Post(String title, String filePath, String description, String ownerId, String categoryId) {
        this.title = title;
        this.filePath = filePath;
        this.description = description;
        this.ownerId = ownerId;
        this.categoryId = categoryId;
    }

    public Post(String title, String filePath, String description, String ownerId) {
        this.title = title;
        this.filePath = filePath;
        this.description = description;
        this.ownerId = ownerId;
    }


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", filePath='" + filePath + '\'' +
                ", description='" + description + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", comments=" + comments +

                ", likes=" + likes +
                '}';
    }
}
