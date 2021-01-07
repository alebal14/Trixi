package com.trixibackend.collections;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.trixibackend.entity.Pet;
import com.trixibackend.entity.Post;
import com.trixibackend.entity.User;
import jdk.jfr.Timestamp;
import org.apache.commons.fileupload.FileItem;
import org.bson.conversions.Bson;
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
import static com.mongodb.client.model.Filters.gte;

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
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public DeleteResult deleteUser(String id) {
        var u = findUserById(id);

        // delete this user from other user's followings list
        var followers = u.getFollowers();
        for(User aFollower :followers){
            userColl.updateOne(eq("_id",aFollower.getId()),Updates.pull("followingsUser",new BasicDBObject("_id", u.getId())));
            System.out.println(u.getUserName() + " is deleted from  " + aFollower.getUserName() + "'s followingsUsers list");

        }
        // delete this user from other user's followers list
        var followings = u.getFollowingsUser();
        for(User aFollowing :followings){
            userColl.updateOne(eq("_id",aFollowing.getId()),Updates.pull("followers",new BasicDBObject("_id", u.getId())));
            System.out.println(u.getUserName() + " is deleted from  " + aFollowing.getUserName() + "'s followers list");

        }
        // delete this user from pet's followers list
        var petFollowings = u.getFollowingsPet();
        for(Pet p:petFollowings){
            petHandler.getPetColl().updateOne(eq("_id",p.getId()),Updates.pull("followers",new BasicDBObject("_id", u.getId())));
            System.out.println(u.getUserName() + " is deleted from  " + p.getName() + "'s followers list");

        }
        //delete user
        Bson userTobeDeleted = eq("_id", new ObjectId(id));
        DeleteResult deletedUser = userColl.deleteOne(userTobeDeleted);
        System.out.println("user deleted: " + deletedUser);

        //delete user's post
        Bson posts = eq("ownerId", id);
        DeleteResult deletedPost = postHandler.getPostColl().deleteMany(posts);
        System.out.println("user's post deleted: " + deletedPost);

        //delete user's pet and pet's post
        var pets = petHandler.findPetsByOwner(id);
        for (Pet p : pets) {
            System.out.println("------------ " + p.getName() + "----------------------");
            petHandler.deletePet(p.getUid(),userColl);
        }


    // remove this users comments and likes for other user's/pet's posts
        var allPosts = postHandler.getAllPosts();
        for (Post p : allPosts) {
            var comments = p.getComments()
                    .stream()
                    .filter(comment -> comment.getUserId().equals(id))
                    .collect(Collectors.toList());
            postHandler.getPostColl().updateOne(eq("_id", p.getId()), Updates.pullAll("comments", comments));
            var likes = p.getLikes()
                    .stream()
                    .filter(like -> like.getUserId().equals(id))
                    .collect(Collectors.toList());
            postHandler.getPostColl().updateOne(eq("_id",p.getId()),Updates.pullAll("likes",likes));
        }


        return deletedUser;

    }


    public List<Post> findUserFollowingPostList(User user) {

        List<User> getFollowingUser = user.getFollowingsUser();
        List<Pet> getFollowingPet = user.getFollowingsPet();

        List<Post> allPostFromDB = postHandler.getAllPosts();
        System.out.println(allPostFromDB);

        Set<String> userid =
                getFollowingUser.stream()
                        .map(User::getUid)
                        .collect(Collectors.toSet());

        Set<String> petid = getFollowingPet.stream()
                .map(Pet::getUid)
                .collect(Collectors.toSet());

        List<String> concatlist = Stream.concat(userid.stream(), petid.stream())
                .collect(Collectors.toList());

        List<Post> listOutput =
                allPostFromDB.stream()
                        .filter(e -> concatlist.contains(e.getOwnerId()))
                        .sorted(Collections.reverseOrder(Comparator.comparing(Post::getUid)))
                        .collect(Collectors.toList());

        return listOutput;
    }

    public User findUserByNameOrEmail(User loggedInUser) {
        String emailOrUsername;
        String fieldname;
        if (loggedInUser.getUserName() == "" || loggedInUser.getUserName() == null) {
            emailOrUsername = loggedInUser.getEmail();
            fieldname = "email";
        } else {
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

    public User updateFollowPetList(User user, Pet following) {
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
        User j = user;
        //Empty the list of the copy
        makeUsersListEmpty(j);
        //update the pet's list
        petHandler.getPetColl().updateOne(eq("_id", following.getId()), Updates.addToSet("followers", j));


        User updatedUser = findUserById(user.getUid());
        System.out.println("---------------------------------------------------------------");
        System.out.println(updatedUser);
        return updatedUser;
    }

    public User updateFollowUserList(User user, User following) {
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
        User e = user;
        //Empty the list of the copy
        makeUsersListEmpty(e);
        //update the user's list
        userColl.updateOne(eq("_id", following.getId()), Updates.addToSet("followers", e));

        User updatedUser = findUserById(user.getUid());
        System.out.println("---------------------------------------------------------------");
        System.out.println(updatedUser);
        return updatedUser;
    }

    public User removeFromFollowPetList(User user, Pet following) {
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

    public User removeFromFollowUserList(User user, User following) {
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


    public List<Post> discoverList(User user){

        List<Post> allPostFromDB = postHandler.getAllPosts();

         List<Post> getLikedPost = allPostFromDB.stream()
                 .filter(e -> e.getLikes().stream().anyMatch(f -> f.getUserId().contains(user.getUid())))
                 .collect(Collectors.toList());

        System.out.println("liked post: " + getLikedPost);

        //get user's post
         List<Post> getUserPost = postHandler.findPostsByOwner(user.getUid());

        System.out.println("My post: " + getUserPost);

        List<User> getFollowingUser = user.getFollowingsUser();
        List<Pet>  getFollowingPet = user.getFollowingsPet();

        Set<String> userid =
                getFollowingUser.stream()
                        .map(User::getUid)
                        .collect(Collectors.toSet());

        Set<String> petid = getFollowingPet.stream()
                .map(Pet::getUid)
                .collect(Collectors.toSet());

        List<String> concatlist = Stream.concat(userid.stream(),petid.stream())
                .collect(Collectors.toList());

        List<Post> getFollowingList =
                allPostFromDB.stream()
                        .filter(e -> concatlist.contains(e.getOwnerId()))
                        .collect(Collectors.toList());

        System.out.println("Following post: " + getFollowingList);


        List<Post> allList = new ArrayList<>();

        allList.addAll(getFollowingList);
        allList.addAll(getUserPost);
        allList.addAll(getLikedPost);

        Set<String> Categories = allList.stream()
                .map(Post::getCategoryName)
                .collect(Collectors.toSet());

        System.out.println("kategories 1 : " + Categories);

        List<Post> allListOwnerId = new ArrayList<>();
        allListOwnerId.addAll(getFollowingList);
        allListOwnerId.addAll(getUserPost);

        Set<String> getIds = allListOwnerId.stream()
                .map(Post::getOwnerId)
                .collect(Collectors.toSet());

        System.out.println("get ownerids " + getIds);

        Set<String> postIds = getLikedPost.stream()
                .map(Post::getUid)
                .collect(Collectors.toSet());

        System.out.println("get liked post: " + postIds);

        System.out.println("all post: " + allPostFromDB.size());

         List<Post> removePost =  allPostFromDB.stream()
                 .filter(e -> !getIds.contains(e.getOwnerId()))
                 .filter(f -> !postIds.contains(f.getUid()))
                 .collect(Collectors.toList());

        System.out.println("list without: " +removePost.size());


         List<Post> resultList = removePost.stream()
                 .filter(e -> Categories.contains(e.getCategoryName()))
                 .sorted(Collections.reverseOrder(Comparator.comparing(Post::getId)))
                 .limit(50)
                 .sorted(Collections.reverseOrder(Comparator.comparing(f ->  f.getLikes().size())))
                 .collect(Collectors.toList());

        System.out.println("endList: " + resultList.size());

         if(resultList.isEmpty()){
             return allPostFromDB.stream().limit(50)
                     .sorted(Collections.reverseOrder(Comparator.comparing(Post::getId)))
                     .limit(50)
                     .sorted(Collections.reverseOrder(Comparator.comparing(f ->  f.getLikes().size())))
                     .collect(Collectors.toList());
         }


        return resultList;
    }

    private void makeUsersListEmpty(User u){

        u.setFollowingsUser(null);
        u.setFollowingsPet(null);
        u.setFollowers(null);
        u.setPets(null);
        u.setPosts(null);
    }

    private void makePetsListEmpty(Pet p) {
        p.setFollowers(null);
        p.setPosts(null);
    }


}
