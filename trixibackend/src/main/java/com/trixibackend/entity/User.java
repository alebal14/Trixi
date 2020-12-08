package com.trixibackend.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class User extends UserPet{
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

    private List<UserPet> followings = new ArrayList<>();
    private List<User> followers = new ArrayList<>();



    public User(){

    }

    /*public User(String userName, String email, String password,String role) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }*/

    private UserPet prepareToAdd(UserPet userPet){
        ObjectMapper mapper = new ObjectMapper();
        UserPet objectToAdd = null;

        try {
            //This is a shallow copy
            String json = mapper.writeValueAsString(userPet);
            objectToAdd = mapper.readValue(json, userPet.getClass());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if(userPet instanceof User){
            ((User) objectToAdd).setPets(null);
            ((User)objectToAdd).setPosts(null);
            //((User)objectToAdd).setFollowings(null);
            ((User)objectToAdd).setFollowers(null);
        }  else if (userPet instanceof Pet) {
            ((Pet)objectToAdd).setPosts(null);
            ((Pet)objectToAdd).setFollowers(null);
        }
        /*switch (userPet.getClass().getSimpleName()){
            case "User":


                break;
            case "Pet":
                (
                break;
        }
*/
        return objectToAdd;
    }

    public void addToFollowings(UserPet userPet){
        followings.add(prepareToAdd(userPet));
    }

    public void addToFollowers(User user){
        followers.add((User) prepareToAdd(user));
    }




    public List<UserPet> getFollowings() {
        return followings;
    }

    public void setFollowings(List<UserPet> followings) {
        this.followings = followings;
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
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", role='" + role + '\'' +
                ", pets=" + pets +
                ", posts=" + posts +
                //", followings=" + followings +
                ", followers=" + followers +
                '}';
    }
}
