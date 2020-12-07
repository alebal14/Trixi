package com.trixibackend.entity;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Pet {
    private ObjectId id;



    private String uid;
    private String name;
    private int age;
    private String gender;
    private String breed;
    private String bio;
    private String imageUrl;

    private String ownerId;
    private String petTypeId;

    private List<Post> posts = new ArrayList<>();
    private List<ObjectId> postIds = new ArrayList<>();

    public Pet(){

    }

    public Pet(String name, int age, String gender, String breed, String bio, String imageUrl, String ownerId) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPetTypeId() {
        return petTypeId;
    }

    public void setPetTypeId(String petTypeId) {
        this.petTypeId = petTypeId;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<ObjectId> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<ObjectId> postIds) {
        this.postIds = postIds;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", breed='" + breed + '\'' +
                ", bio='" + bio + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", petTypeId='" + petTypeId + '\'' +
                ", posts=" + posts +
                ", postIds=" + postIds +
                '}';
    }
}
