package com.trixibackend.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class User {
    private ObjectId id;
    private String uid;
    private String userName;
    private String email;
    private String password;
    private String bio;
    private String imageUrl;

    private String role;

    private List<Pet> pets = new ArrayList<>();

    private List<Post> posts = new ArrayList<>();

    private List<Object> followings = new ArrayList<>();
    private List<User> followers = new ArrayList<>();

    private Object prepareToAdd(Object object){
        ObjectMapper mapper = new ObjectMapper();
        Object objectToAdd = null;

        try {
            //This is a shallow copy
            String json = mapper.writeValueAsString(object);
            objectToAdd = mapper.readValue(json, object.getClass());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        switch (object.getClass().getSimpleName()){
            case "User":
                ((User) objectToAdd).setPets(null);
                ((User)objectToAdd).setPosts(null);
                ((User)objectToAdd).setFollowings(null);
                ((User)objectToAdd).setFollowers(null);

                break;
            case "Pet":
                ((Pet)objectToAdd).setPosts(null);
                ((Pet)objectToAdd).setFollowers(null);
                break;
        }

        return objectToAdd;
    }

    public void addToFollowing(Object object){
        followings.add(prepareToAdd(object));


    }

    public void addToFollower(User user){
        followers.add((User) prepareToAdd(user));
    }


    /* TODO
    *   Create List for Followers and Following */

    public List<Object> getFollowings() {
        return followings;
    }

    public void setFollowings(List<Object> followings) {
        this.followings = followings;
    }

    public User(){

    }

    public User(String userName, String email, String password,String role) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }



    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", name='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", role='" + role + '\'' +
                ", pets=" + pets +

                ", posts=" + posts +

                '}';
    }
}
