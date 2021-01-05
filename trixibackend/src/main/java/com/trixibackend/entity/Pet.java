package com.trixibackend.entity;


import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


public class Pet{
    private ObjectId id;
    private String uid;
    private String name;
    private String age;
    private String gender;
    private String breed;
    private String bio;
    private String imageUrl;

    private String ownerId;
    private String petType;

    private List<Post> posts = new ArrayList<>();
    private List<User> followers = new ArrayList<>();


    public Pet() {

    }

    public Pet(String name, String age, String gender, String breed, String bio, String petType, String imageUrl, String ownerId) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
        this.bio = bio;
        this.petType = petType;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
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

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
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
                ", petTypeId='" + petType + '\'' +
                ", posts=" + posts +
                ", followers=" + followers +
                '}';
    }
}