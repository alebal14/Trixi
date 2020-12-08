package com.trixibackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


public class Pet extends UserPet{
    //private ObjectId id;
    //private String uid;
    private String name;
    private int age;
    private String gender;
    private String breed;
    private String bio;
    private String imageUrl;

    private String ownerId;
    private String petTypeId;

    private List<Post> posts = new ArrayList<>();
    private List<User> followers = new ArrayList<>();

    public Pet() {
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

    private User prepareToAdd(User user) {
        ObjectMapper mapper = new ObjectMapper();
        User userToAdd = null;

        try {
            //This is a shallow copy
            String json = mapper.writeValueAsString(user);
            userToAdd = mapper.readValue(json, User.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        userToAdd.setPets(null);
        userToAdd.setPosts(null);
        userToAdd.setFollowings(null);
        userToAdd.setFollowers(null);

        return userToAdd;
    }

    public void addToFollowers(User user){
        followers.add(prepareToAdd(user));
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
    @JsonIgnore
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
                ", petTypeId='" + petTypeId + '\'' +
                ", posts=" + posts +
                '}';
    }
}