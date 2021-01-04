package com.trixibackend.collections;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.trixibackend.entity.Pet;
import com.trixibackend.entity.Post;
import com.trixibackend.entity.User;
import jdk.jfr.Timestamp;
import org.apache.commons.fileupload.FileItem;
import org.bson.types.ObjectId;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mongodb.client.model.Filters.eq;

public class UserHandler {
    private MongoCollection<User> userColl;
    private PostHandler postHandler;
    private PetHandler petHandler;


    public UserHandler(MongoDatabase database) {
        userColl = database.getCollection("users", User.class);
        postHandler = new PostHandler(database);
        petHandler = new PetHandler(database);

    }

    public MongoCollection<User> getUserColl() {
        return userColl;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            FindIterable<User> usersIter = userColl.find();
            usersIter.forEach(users::add);
            users.forEach(user -> {
                user.setUid(user.getId().toString());
//                user.setPosts(postHandler.findPostsByOwner(user.getUid()));
//                user.setPets(petHandler.findPetsByOwner(user.getUid()));
//                user.getPosts().forEach(post -> {
//                            post.setUid(post.getId().toString());
//                            post.setLikes(postHandler.getLikeHandler().findLikesByPostId(post.getUid()));
//                            post.setComments(postHandler.getCommentHandler().findCommentsByPostId(post.getUid()));
//                        }
//                );
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public User findUserById(String id) {
        try {
            var userIter = userColl.find(eq("_id", new ObjectId(id)));
            var user = userIter.first();
            if (user == null) return null;
            user.setUid(user.getId().toString());
//            user.setPosts(postHandler.findPostsByOwner(user.getUid()));
//            user.setPets(petHandler.findPetsByOwner(user.getUid()));
//            user.getPosts().forEach(post -> {
//                post.setUid(post.getId().toString());
//                post.setLikes(postHandler.getLikeHandler().findLikesByPostId(post.getUid()));
//                post.setComments(postHandler.getCommentHandler().findCommentsByPostId(post.getUid()));
//            });
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Post> findUserFollowingPostList(User user){

        List<User> getFollowingUser = user.getFollowingsUser();
        List<Pet>  getFollowingPet = user.getFollowingsPet();

        List<Post> allPostFromDB = postHandler.getAllPosts();
        System.out.println(allPostFromDB);

        Set<String> userid =
                getFollowingUser.stream()
                        .map(User::getUid)
                        .collect(Collectors.toSet());

        Set<String> petid = getFollowingPet.stream()
                .map(Pet::getUid)
                .collect(Collectors.toSet());

        List<String> concatlist = Stream.concat(userid.stream(),petid.stream())
                .collect(Collectors.toList());

        List<Post> listOutput =
                allPostFromDB.stream()
                        .filter(e -> concatlist.contains(e.getOwnerId()))
                        .sorted(Collections.reverseOrder(Comparator.comparing(o -> o.getUid())))
                        .collect(Collectors.toList());

        return listOutput;
    }

    public User findUserByNameOrEmail(User loggedInUser) {
        String emailOrUsername;
        String fieldname;
        if(loggedInUser.getUserName() == "" || loggedInUser.getUserName() == null){
            emailOrUsername = loggedInUser.getEmail();
            fieldname = "email";
        }else{
            emailOrUsername = loggedInUser.getUserName();
            fieldname = "userName";
        }
        try {
            var user = userColl.find(eq(fieldname, emailOrUsername)).first();

           if (user == null) return null;

           return user;

        } catch (Exception e) {
            return null;
        }
    }

    public User updateFollowPetList(User user, Pet following){
        AtomicBoolean found = new AtomicBoolean(false);

        user.getFollowingsPet().forEach(u -> {
            if (u.getId().equals(following.getId())) {
                found.set(true);
            }
        });

        if (found.get() == true) {
            return null;
        }

        //Shadow copy
        Pet p = following;
        //Empty the list of the copy
        makePetsListEmpty(p);
        //update the user's list
        userColl.updateOne(eq("_id", user.getId()), Updates.addToSet("followingsPet", p));

        //Shadow copy
        User j  = user;
        //Empty the list of the copy
        makeUsersListEmpty(j);
        //update the pet's list
        petHandler.getPetColl().updateOne(eq("_id", following.getId()), Updates.addToSet("followers", j));


        User updatedUser = findUserById(user.getUid());
        System.out.println("---------------------------------------------------------------");
        System.out.println(updatedUser);
        return updatedUser;
    }

    public User updateFollowUserList(User user, User following){
        AtomicBoolean found = new AtomicBoolean(false);

        user.getFollowingsUser().forEach(u -> {
            if (u.getId().equals(following.getId())) {
                found.set(true);
            }
        });

        if (found.get() == true) {
            return null;
        }

        //Shadow copy
        User u = following;
        //Empty the list of the copy
        makeUsersListEmpty(u);
        //update the user's list
        userColl.updateOne(eq("_id", user.getId()), Updates.addToSet("followingsUser", u));

        //Shadow copy
        User e  = user;
        //Empty the list of the copy
        makeUsersListEmpty(e);
        //update the user's list
        userColl.updateOne(eq("_id", following.getId()), Updates.addToSet("followers", e));

        User updatedUser = findUserById(user.getUid());
        System.out.println("---------------------------------------------------------------");
        System.out.println(updatedUser);
        return updatedUser;
    }

    public User removeFromFollowPetList(User user, Pet following){
        AtomicBoolean found = new AtomicBoolean(false);

        user.getFollowingsPet().forEach(u -> {
            if (u.getId().equals(following.getId())) {
                found.set(true);
            }
        });

        if (found.get() == false) {
            return null;
        }

        //remove from the user's list and update
        userColl.updateOne(eq("_id", user.getId()), Updates.pull("followingsPet", new BasicDBObject("_id", following.getId())));
        //remove from the pet's list and update
        petHandler.getPetColl().updateOne(eq("_id", following.getId()), Updates.pull("followers", new BasicDBObject("_id", user.getId())));

        User updatedUser = findUserById(user.getUid());
        return updatedUser;
    }

    public User removeFromFollowUserList(User user, User following){
        AtomicBoolean found = new AtomicBoolean(false);

        user.getFollowingsUser().forEach(u -> {
            if (u.getUid().equals(following.getUid())) {
                found.set(true);
            }
        });

        if (found.get() == false) {
            return null;
        }

        //remove from the user's list and update the list
        userColl.updateOne(eq("_id", user.getId()), Updates.pull("followingsUser", new BasicDBObject("_id", following.getId())));
        //remove from the following's list
        userColl.updateOne(eq("_id", following.getId()), Updates.pull("followers", new BasicDBObject("_id", user.getId())));

        User updatedUser = findUserById(user.getUid());
        return updatedUser;
    }

    private void makeUsersListEmpty(User u){
        u.setFollowingsUser(null);
        u.setFollowingsPet(null);
        u.setFollowers(null);
        u.setPets(null);
        u.setPosts(null);
    }

    private void makePetsListEmpty(Pet p){
        p.setFollowers(null);
        p.setPosts(null);
    }




}
